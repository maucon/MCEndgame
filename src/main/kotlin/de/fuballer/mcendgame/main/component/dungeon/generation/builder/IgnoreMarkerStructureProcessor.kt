package de.fuballer.mcendgame.main.component.dungeon.generation.builder

import de.fuballer.mcendgame.main.component.dungeon.generation.DungeonGenerationSettings
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate

class IgnoreMarkerStructureProcessor : StructureProcessor() {
    override fun processBlock(
        world: LevelReader,
        pos: BlockPos,
        pivot: BlockPos,
        originalBlockInfo: StructureTemplate.StructureBlockInfo,
        currentBlockInfo: StructureTemplate.StructureBlockInfo,
        data: StructurePlaceSettings
    ): StructureTemplate.StructureBlockInfo? {
        return if (DungeonGenerationSettings.MARKER_BLOCKS.contains(originalBlockInfo.state.block)) {
            null
        } else {
            super.processBlock(world, pos, pivot, originalBlockInfo, currentBlockInfo, data)
        }
    }

    override fun getType(): StructureProcessorType<*> = StructureProcessorType.BLOCK_IGNORE
}