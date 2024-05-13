package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object DungeonTypeSettings {
    const val COMMAND_NAME = "dungeon-type"
    val PLAYER_NO_DUNGEON_TYPE = "${ChatColor.RED}Player has no dungeon type!"

    fun getPrefix(player: Player) =
        "${ChatColor.AQUA}${ChatColor.ITALIC}${player.name}${ChatColor.RESET}${ChatColor.AQUA}'s next dungeon type: ["

    val SUFFIX = "${ChatColor.AQUA}]"

    val DUNGEON_TYPE_WEIGHTS = listOf(
        RandomOption(1, DungeonType.STANDARD)
    )
}
