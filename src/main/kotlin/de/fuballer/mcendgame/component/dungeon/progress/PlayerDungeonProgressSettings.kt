package de.fuballer.mcendgame.component.dungeon.progress

import org.bukkit.ChatColor

object PlayerDungeonProgressSettings {
    const val DUNGEON_LEVEL_INCREASE_THRESHOLD = 3
    const val COMMAND_NAME = "dungeon-progress"

    val PLAYER_NO_PROGRESS_MESSAGE = "${ChatColor.RED}Player has no dungeon progress!"

    fun getDungeonProgressMessage(playerName: String, level: Int, progress: Int) =
        "${ChatColor.GREEN}$playerName is Level: ${level}, Progress: ${progress}/${PlayerDungeonProgressSettings.DUNGEON_LEVEL_INCREASE_THRESHOLD}"

    fun getNewDungeonProgressMessage(playerName: String, level: Int, progress: Int) =
        "${ChatColor.GREEN}$playerName is now Level: ${level}, Progress: ${progress}/${PlayerDungeonProgressSettings.DUNGEON_LEVEL_INCREASE_THRESHOLD}"

    private val dungeonCompleteMessage = "${ChatColor.BOLD}${ChatColor.GOLD}Dungeon completed! "
    val NO_PROGRESS_MESSAGE = "$dungeonCompleteMessage${ChatColor.RESET}${ChatColor.RED}Dungeon level is too low to grant progress."

    fun getProgressMessage(level: Int, progress: Int) =
        "$dungeonCompleteMessage${ChatColor.RESET}${ChatColor.BLUE}Level: $level, Progress: $progress/${DUNGEON_LEVEL_INCREASE_THRESHOLD}"

    fun getRegressMessage(level: Int, progress: Int) =
        "${ChatColor.BOLD}${ChatColor.RED}You died! ${ChatColor.RESET}${ChatColor.BLUE}You are now Level: ${level}, Progress: ${progress}/${DUNGEON_LEVEL_INCREASE_THRESHOLD}"
}
