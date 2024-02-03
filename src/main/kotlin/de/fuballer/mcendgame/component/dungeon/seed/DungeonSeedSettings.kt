package de.fuballer.mcendgame.component.dungeon.seed

import org.bukkit.ChatColor
import org.bukkit.entity.Player

object DungeonSeedSettings {
    const val COMMAND_NAME = "dungeon-seed"

    val PLAYER_NO_SEED = "${ChatColor.RED}Player has no seed!"
    val INVALID_SEED = "${ChatColor.RED}Invalid seed! Seed must be of length 1 to 64!"

    fun getPrefix(player: Player) =
        "${ChatColor.AQUA}${ChatColor.ITALIC}${player.name}${ChatColor.RESET}${ChatColor.AQUA}'s next dungeon seed: ["

    val SUFFIX = "${ChatColor.AQUA}]"
}