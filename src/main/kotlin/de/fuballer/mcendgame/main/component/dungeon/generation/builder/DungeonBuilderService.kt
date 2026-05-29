package de.fuballer.mcendgame.main.component.dungeon.generation.builder

import de.fuballer.mcendgame.main.component.dungeon.generation.data.PlaceableBlock
import de.fuballer.mcendgame.main.component.dungeon.generation.data.PlaceableRoom
import de.fuballer.mcendgame.main.util.RotationUtil
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.rotateY90
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.toBlockPos
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplate
import net.minecraft.util.BlockMirror
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

@Injectable
class DungeonBuilderService {
    fun build(
        world: ServerWorld,
        rooms: List<PlaceableRoom>
    ) {
        for (room in rooms) {
            val rotDeg = room.rotation90 * 90.0
            placeTemplate(world, room.type.template, room.position, rotDeg, room.type.mirrored)

            for (extension in room.type.extensions) {
                val rotatedOffset = extension.offset.rotateY90(room.rotation90)
                val position = rotatedOffset.add(room.position)
                placeTemplate(world, extension.template, position, rotDeg, room.type.mirrored)
            }

            placeBlocks(world, room.position, room.rotation90, room.extraBlocks)
        }
    }

    private fun placeTemplate(
        world: ServerWorld,
        template: StructureTemplate,
        position: Vec3i,
        rotation: Double, // in degree
        mirrored: Boolean
    ) {
        val blockRotation = RotationUtil.getAsRotation(rotation)
        val structurePlacementData = StructurePlacementData()
            .addProcessor(IgnoreMarkerStructureProcessor())
            .setRotation(RotationUtil.getAsRotation(rotation))
            .setMirror(if (mirrored) BlockMirror.FRONT_BACK else BlockMirror.NONE)

        val offset = if (mirrored) {
            BlockPos(template.size.x - 1, 0, 0).rotate(blockRotation)
        } else {
            BlockPos.ORIGIN
        }

        val pos = position.toBlockPos()
            .add(offset)

        template.place(world, pos, pos, structurePlacementData, world.random, 2)
    }

    private fun placeBlocks(
        world: ServerWorld,
        offset: Vec3i,
        rotation90: Int,
        blocks: List<PlaceableBlock>
    ) = blocks.forEach { placeBlock(world, offset, rotation90, it) }

    private fun placeBlock(
        world: ServerWorld,
        offset: Vec3i,
        rotation90: Int,
        block: PlaceableBlock
    ) {
        val blockPos = block.getBlockPos(offset, rotation90)

        var state = block.block.defaultState
        if (state.contains(Properties.ROTATION)) {
            val roomRot16 = rotation90 * 4
            val direction = (block.rotation16 + roomRot16) % 16
            state = state.with(Properties.ROTATION, direction)
        }

        world.setBlockState(blockPos, state)
    }
}