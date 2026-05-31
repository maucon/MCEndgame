package de.fuballer.mcendgame.main.component.dungeon.seed

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.util.random.RandomOption

object DungeonSeedSettings {
    val DUNGEON_TYPES = listOf(
        RandomOption(1, DungeonType.STRONGHOLD),
        RandomOption(1, DungeonType.NETHER),
        RandomOption(1, DungeonType.DESERT),
    )
}