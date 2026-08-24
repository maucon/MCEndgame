package de.fuballer.mcendgame.main.component.dungeon.generation

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

object DungeonGenerationSettings {
    val START_POS_MARKER: Block = Blocks.WOOL.green
    val MONSTER_MARKER: Block = Blocks.WOOL.white()
    val BOSS_MARKER: Block = Blocks.DRAGON_HEAD
    val DOOR_MARKER: Block = Blocks.WOOL.black()
    val ENCOUNTER_MARKER: Block = Blocks.WOOL.yellow()
    val START_ENCOUNTER_MARKER: Block = Blocks.WOOL.lime()

    val MARKER_BLOCKS = listOf(START_POS_MARKER, MONSTER_MARKER, BOSS_MARKER, DOOR_MARKER, ENCOUNTER_MARKER, START_ENCOUNTER_MARKER)

    val IGNORED_BLOCKS = MARKER_BLOCKS + listOf(Blocks.STRUCTURE_BLOCK)
}