package de.fuballer.mcendgame.component.dungeon.seed

import org.bukkit.ChatColor

object DungeonSeedSettings {
    const val COMMAND_NAME = "player-dungeon-seed"
    val PLAYER_NO_SEED = "${ChatColor.RED}Player has no seed!"
    val INVALID_SEED = "${ChatColor.RED}Invalid seed! Seed must be of length 1 to 64"

    fun getSeedMessage(playerName: String, seed: String) =
        "${ChatColor.AQUA}${playerName}'s next dungeon seed will be '$seed'"
}