package de.fuballer.mcendgame.component.dungeon.remaining

import org.bukkit.ChatColor

object RemainingSettings {
    const val COMMAND_NAME = "dungeon-remaining"

    val REMAINING_COLOR = ChatColor.RED
    val REMAINING_MESSAGE = ChatColor.BLUE.toString() + "monsters remaining."
    val BOSS_ALIVE = ChatColor.RED.toString() + "(Boss alive)"
    val BOSS_DEAD = ChatColor.GREEN.toString() + "(Boss killed)"
    val NOT_IN_DUNGEON_ERROR = ChatColor.RED.toString() + "Currently not in a dungeon."
    val GENERAL_ERROR = ChatColor.RED.toString() + "An error occurred."
}