package de.fuballer.mcendgame.main.component.dungeon.generation.builder

import de.fuballer.mcendgame.main.component.dungeon.generation.data.PlaceableBlock
import de.fuballer.mcendgame.main.component.dungeon.generation.data.PlaceableRoom
import de.fuballer.mcendgame.main.util.RotationUtil
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.rotateY90
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.toBlockPos
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate

@Injectable
class DungeonBuilderService {
    fun build(
        world: ServerLevel,
        rooms: List<PlaceableRoom>
    ) {
        for (room in rooms) {
            val rotDeg = room.rotation90 * 90.0
            placeTemplate(world, room.type.template, room.position, rotDeg, room.type.mirrored)

            for (extension in room.type.extensions) {
                val rotatedOffset = extension.offset.rotateY90(room.rotation90)
                val position = rotatedOffset.offset(room.position)
                placeTemplate(world, extension.template, position, rotDeg, room.type.mirrored)
            }

            placeBlocks(world, room.position, room.rotation90, room.extraBlocks)
        }
    }

    private fun placeTemplate(
        world: ServerLevel,
        template: StructureTemplate,
        position: Vec3i,
        rotation: Double, // in degree
        mirrored: Boolean
    ) {
        val blockRotation = RotationUtil.getAsRotation(rotation)
        val structurePlacementData = StructurePlaceSettings()
            .addProcessor(IgnoreMarkerStructureProcessor())
            .setRotation(RotationUtil.getAsRotation(rotation))
            .setMirror(if (mirrored) Mirror.FRONT_BACK else Mirror.NONE)

        val offset = if (mirrored) {
            BlockPos(template.size.x - 1, 0, 0).rotate(blockRotation)
        } else {
            BlockPos.ZERO
        }

        val pos = position.toBlockPos()
            .offset(offset)

        template.placeInWorld(world, pos, pos, structurePlacementData, world.random, 2)
    }

    private fun placeBlocks(
        world: ServerLevel,
        offset: Vec3i,
        rotation90: Int,
        blocks: List<PlaceableBlock>
    ) = blocks.forEach { placeBlock(world, offset, rotation90, it) }

    private fun placeBlock(
        world: ServerLevel,
        offset: Vec3i,
        rotation90: Int,
        block: PlaceableBlock
    ) {
        val blockPos = block.getBlockPos(offset, rotation90)

        var state = block.block.defaultBlockState()
        if (state.hasProperty(BlockStateProperties.ROTATION_16)) {
            val roomRot16 = rotation90 * 4
            val direction = (block.rotation16 + roomRot16) % 16
            state = state.setValue(BlockStateProperties.ROTATION_16, direction)
        }

        world.setBlockAndUpdate(blockPos, state)
    }
}