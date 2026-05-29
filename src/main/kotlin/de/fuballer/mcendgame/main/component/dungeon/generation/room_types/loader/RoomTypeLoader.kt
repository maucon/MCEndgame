package de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader

import de.fuballer.mcendgame.main.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.main.component.dungeon.generation.data.*
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.clone
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.max
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.block.Block
import net.minecraft.state.property.Properties
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplate
import net.minecraft.structure.StructureTemplateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

object RoomTypeLoader {
    fun load(
        templateManager: StructureTemplateManager,
        path: String,
        extensionPaths: Map<String, BlockPos> = mapOf()
    ): RoomType {
        val template = loadTemplate(templateManager, path)

        if (extensionPaths.isEmpty()) {
            val markerPoints = getMarkers(template)
            return RoomType(path, template, markerPoints)
        }

        val extensions = extensionPaths.keys.stream()
            .map { RoomTemplateExtension(loadTemplate(templateManager, it), extensionPaths[it]!!) }
            .toList()

        val size = getTotalSize(template, extensions)

        val markerPoints = getMarkers(template, size = size)
        extensions.forEach { markerPoints.add(getMarkers(it.template, it.offset, size)) }

        return RoomType(path, template, markerPoints, size, extensions)
    }

    fun loadWithMirrored(
        templateManager: StructureTemplateManager,
        path: String,
    ): List<RoomType> {
        val original = load(templateManager, path)
        val size = original.size

        val mirrored = RoomType(
            path = original.path,
            template = original.template,
            markerPoints = MirrorUtil.mirrorMarkerPoints(original.markerPoints, size),
            size = original.size,
            mirrored = true
        )

        return listOf(original, mirrored)
    }

    private fun loadTemplate(
        templateManager: StructureTemplateManager,
        path: String
    ): StructureTemplate {
        val id = IdentifierUtil.default(path)
        val templateOptional = templateManager.getTemplate(id)

        if (templateOptional.isEmpty) {
            throw IllegalStateException("Couldn't load template: $path")
        }

        return templateOptional.get()
    }

    private fun getTotalSize(main: StructureTemplate, extensions: List<RoomTemplateExtension>): Vec3i {
        var size = main.size.clone()

        for (extension in extensions) {
            size = size.max(extension.offset.add(extension.template.size))
        }

        return size
    }

    private fun getMarkers(
        template: StructureTemplate,
        offset: Vec3i = Vec3i.ZERO,
        size: Vec3i? = null,
    ): RoomMarkerPoints {
        val startPosMarkerInfos = getMarkerInfos(template, DungeonGenerationSettings.START_POS_MARKER)
        val startPos = if (startPosMarkerInfos.isEmpty()) null else startPosMarkerInfos.first().pos.add(offset)

        val monsterPosMarkerInfos = getMarkerInfos(template, DungeonGenerationSettings.MONSTER_MARKER)
        val monsterPos =
            monsterPosMarkerInfos.stream().map { SpawnPosition(it.pos.add(offset)) }.toList()

        val bossPosMarkerInfos = getMarkerInfos(template, DungeonGenerationSettings.BOSS_MARKER)
        val bossPos = bossPosMarkerInfos.stream().map {
            SpawnPosition(
                it.pos.add(offset),
                (it.state.get(Properties.ROTATION) * 22.5 + 180.0) % 360 // 0 rot -> 180 yaw
            )
        }.toList()

        val doorPosMarkerInfos = getMarkerInfos(template, DungeonGenerationSettings.DOOR_MARKER)
        val doorPos =
            doorPosMarkerInfos.stream()
                .map { getDoor(it.pos.add(offset), size ?: template.size) }
                .toList()

        val encounterPosMarkerInfos = getMarkerInfos(template, DungeonGenerationSettings.ENCOUNTER_MARKER)
        val encounterPos = encounterPosMarkerInfos.stream().map { it.pos.add(offset) }.toList()
        val startEncounterPosMarkerInfos = getMarkerInfos(template, DungeonGenerationSettings.START_ENCOUNTER_MARKER)
        val startEncounterPos = startEncounterPosMarkerInfos.stream().map { it.pos.add(offset) }.toList()

        return RoomMarkerPoints.fromImmutable(startPos, monsterPos, bossPos, doorPos, encounterPos, startEncounterPos)
    }

    private fun getMarkerInfos(template: StructureTemplate, block: Block) = template.getInfosForBlock(BlockPos(0, 0, 0), StructurePlacementData(), block)

    private fun getDoor(pos: Vec3i, size: Vec3i) = Door(
        pos.clone(),
        getDoorDirection(pos.x, pos.z, size)
    )

    private fun getDoorDirection(x: Int, z: Int, size: Vec3i) =
        if (x == 0) {
            Vec3i(-1, 0, 0)
        } else if (z == 0) {
            Vec3i(0, 0, -1)
        } else if (x == size.x - 1) {
            Vec3i(1, 0, 0)
        } else {
            Vec3i(0, 0, 1)
        }
}