package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.util.random.RandomOption

object DungeonTypeSettings {
    val DUNGEON_TYPE_WEIGHTS = listOf(
        RandomOption(10, DungeonType.CATACOMBS),
        RandomOption(5, DungeonType.CATACOMBS_ALTERNATIVE),
        RandomOption(10, DungeonType.LUSH_CAVE),
        RandomOption(10, DungeonType.MINE),
        RandomOption(10, DungeonType.ICE_CAVE)
    )
}
