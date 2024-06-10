package de.fuballer.mcendgame.component.dungeon.progress

import org.bukkit.ChatColor

object PlayerDungeonProgressSettings {
    const val DUNGEON_LEVEL_INCREASE_THRESHOLD = 3
    const val COMMAND_NAME = "dungeon-progress"

    const val MAX_DUNGEON_TIER = 300 // only for the set progress command

    val PLAYER_NO_PROGRESS_MESSAGE = "${ChatColor.RED}Player has no dungeon progress"

    fun getDungeonProgressMessage(playerName: String, tier: Int, progress: Int) =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$playerName${ChatColor.RESET}${ChatColor.AQUA} is tier: $tier${ChatColor.GRAY},${ChatColor.LIGHT_PURPLE} Progress: ${progress}/$DUNGEON_LEVEL_INCREASE_THRESHOLD${ChatColor.RESET}"

    private val dungeonCompleteMessage = "${ChatColor.BOLD}${ChatColor.GOLD}Dungeon completed! "
    val NO_PROGRESS_MESSAGE = "$dungeonCompleteMessage${ChatColor.RESET}${ChatColor.RED}Dungeon tier is too low to grant progress!"

    fun getProgressMessage(tier: Int, progress: Int) =
        "$dungeonCompleteMessage${ChatColor.RESET}${ChatColor.AQUA}Tier: $tier${ChatColor.GRAY},${ChatColor.LIGHT_PURPLE} Progress: $progress/${DUNGEON_LEVEL_INCREASE_THRESHOLD}${ChatColor.RESET}"

    fun getRegressMessage(tier: Int, progress: Int) =
        "${ChatColor.BOLD}${ChatColor.RED}You died! ${ChatColor.RESET}${ChatColor.AQUA}You are now tier: $tier${ChatColor.GRAY},${ChatColor.LIGHT_PURPLE} Progress: ${progress}/${DUNGEON_LEVEL_INCREASE_THRESHOLD}${ChatColor.RESET}"
}