package de.fuballer.mcendgame.main.component.dungeon.generation

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

object DungeonGenerationSettings {
    val START_POS_MARKER: Block = Blocks.GREEN_WOOL
    val MONSTER_MARKER: Block = Blocks.WHITE_WOOL
    val BOSS_MARKER: Block = Blocks.DRAGON_HEAD
    val DOOR_MARKER: Block = Blocks.BLACK_WOOL
    val ENCOUNTER_MARKER: Block = Blocks.YELLOW_WOOL
    val START_ENCOUNTER_MARKER: Block = Blocks.LIME_WOOL

    val MARKER_BLOCKS = listOf(START_POS_MARKER, MONSTER_MARKER, BOSS_MARKER, DOOR_MARKER, ENCOUNTER_MARKER, START_ENCOUNTER_MARKER)

    val IGNORED_BLOCKS = MARKER_BLOCKS + listOf(Blocks.STRUCTURE_BLOCK)
}