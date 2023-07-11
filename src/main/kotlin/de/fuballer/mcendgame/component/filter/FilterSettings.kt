package de.fuballer.mcendgame.component.filter

import org.bukkit.ChatColor

object FilterSettings {
    const val COMMAND_NAME = "dungeon-filter"

    val FILTER_WINDOW_TITLE = "${ChatColor.BLACK}Filter - (Blacklist)"
    const val FILTER_SIZE = 54 // multiple of 9 between 9 and 54
}
