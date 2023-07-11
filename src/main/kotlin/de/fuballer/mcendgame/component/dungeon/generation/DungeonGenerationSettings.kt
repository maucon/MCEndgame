package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.component.dungeon.generation.data.DungeonType
import de.fuballer.mcendgame.random.RandomOption
import java.io.File

object DungeonGenerationSettings {
    val SCHEMATIC_LOCATION = File(MCEndgame.DATA_FOLDER.toString() + "/schematics")

    var DUNGEON_WIDTH = 10
    var DUNGEON_HEIGHT = 10
    var DUNGEON_JUNCTION_PROBABILITY = .5
    var DUNGEON_MAX_ADJACENT_TILE_DIFF = 6
    var DUNGEON_BOSS_ROOM_X_TILE = DUNGEON_WIDTH / 2

    val DUNGEON_TYPES = listOf(
        RandomOption(10, DungeonType.CATACOMBS),
        RandomOption(5, DungeonType.CATACOMBS_ALTERNATIVE),
        RandomOption(10, DungeonType.LUSH_CAVE),
        RandomOption(10, DungeonType.MINE),
        RandomOption(10, DungeonType.ICE_CAVE)
    )

    const val DUNGEON_Y_POS = 100.0
    const val MOB_Y_POS = DUNGEON_Y_POS + 6.2
    const val PORTAL_Y_POS = DUNGEON_Y_POS + 6.0
}
