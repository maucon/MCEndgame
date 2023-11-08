package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.component.dungeon.type.data.DungeonType
import de.fuballer.mcendgame.util.random.RandomOption

object DungeonTypeSettings {
    val DUNGEON_TYPE_WEIGHTS = listOf(
        RandomOption(1, DungeonType.HELL),
        RandomOption(1, DungeonType.MONSTER),
        RandomOption(1, DungeonType.UNDEAD),
        RandomOption(1, DungeonType.MYTHICAL),
    )
}
