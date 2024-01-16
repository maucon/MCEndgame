package de.fuballer.mcendgame.component.item_info

import org.bukkit.ChatColor

object ItemInfoSettings {
    const val COMMAND_NAME = "item-info"
    val NO_ITEM = "${ChatColor.RED}No Item in Mainhand!"
    val NO_ATTRIBUTES = "${ChatColor.RED}Item can't have attributes!"
    val ITEM_TYPE_COLOR = "${ChatColor.BLACK}${ChatColor.UNDERLINE}"
    val ATTRIBUTE_COLOR = "${ChatColor.DARK_GREEN}"
    val VALUE_COLOR = "${ChatColor.BLACK}"
    const val ATTRIBUTE_NOT_PRESENT = "Not present"
    const val BOOK_AUTHOR = "MCEndgame"
    const val BOOK_TITLE = "ItemStats"
}