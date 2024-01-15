package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.domain.dungeon.DungeonType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor

object DungeonTypeSettings {
    const val COMMAND_NAME = "dungeon-type"
    val PLAYER_NO_DUNGEON_TYPE = "${ChatColor.RED}Player has no dungeon type!"
    val INVALID_DUNGEON_TYPE = "${ChatColor.RED}Invalid dungeon type!"

    fun getDungeonTypeMessage(playerName: String, dungeonType: DungeonType) =
        "${ChatColor.AQUA}${playerName}'s next dungeon will be of type '$dungeonType'"

    val DUNGEON_TYPE_WEIGHTS = listOf(
        RandomOption(1, DungeonType.HELL),
        RandomOption(1, DungeonType.UNDEAD),
        RandomOption(1, DungeonType.MYTHICAL),
        RandomOption(1, DungeonType.FOREST),
    )
}
