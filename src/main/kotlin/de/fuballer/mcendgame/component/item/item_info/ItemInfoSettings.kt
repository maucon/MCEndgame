package de.fuballer.mcendgame.component.item.item_info

import org.bukkit.ChatColor

object ItemInfoSettings {
    const val COMMAND_NAME = "item-info"
    val NO_ITEM = "${ChatColor.RED}No Item in Mainhand!"
    val INVALID_ITEM = "${ChatColor.RED}Invalid item!"
    val ITEM_TYPE_COLOR = "${ChatColor.BLACK}${ChatColor.BOLD}"
    val ATTRIBUTE_COLOR = "${ChatColor.BLUE}"
    val VALUE_COLOR = "${ChatColor.BLACK}"
    const val NOT_ROLLED_TEXT = ""
    const val BOOK_AUTHOR = "MCEndgame"
    const val BOOK_TITLE = "ItemStats"
}