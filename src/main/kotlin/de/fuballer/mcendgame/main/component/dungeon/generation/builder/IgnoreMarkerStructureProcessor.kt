package de.fuballer.mcendgame.main.component.dungeon.generation.builder

import com.mojang.serialization.MapCodec
import de.fuballer.mcendgame.main.component.dungeon.generation.DungeonGenerationSettings
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate

class IgnoreMarkerStructureProcessor : StructureProcessor {
    companion object {
        val MAP_CODEC: MapCodec<IgnoreMarkerStructureProcessor> = MapCodec.unit { IgnoreMarkerStructureProcessor() }
    }

    override fun processBlock(
        level: LevelReader,
        targetPosition: BlockPos,
        referencePos: BlockPos,
        templateRelativePos: BlockPos,
        processedBlockInfo: StructureTemplate.StructureBlockInfo,
        settings: StructurePlaceSettings
    ): StructureTemplate.StructureBlockInfo? {
        return if (DungeonGenerationSettings.IGNORED_BLOCKS.contains(processedBlockInfo.state.block)) {
            null
        } else {
            super.processBlock(level, targetPosition, referencePos, templateRelativePos, processedBlockInfo, settings)
        }
    }

    override fun codec() = MAP_CODEC
}