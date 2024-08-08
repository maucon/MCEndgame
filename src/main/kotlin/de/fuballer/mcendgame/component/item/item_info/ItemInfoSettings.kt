package de.fuballer.mcendgame.component.item.item_info

import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.format.NamedTextColor

object ItemInfoSettings {
    const val COMMAND_NAME = "item-info"
    val NO_ITEM = TextComponent.error("No item in Mainhand")
    val INVALID_ITEM = TextComponent.error("Invalid item")
    val ATTRIBUTE_COLOR: NamedTextColor = NamedTextColor.BLUE
    val VALUE_COLOR: NamedTextColor = NamedTextColor.BLACK
    val BOOK_AUTHOR = PluginConfiguration.plugin().name
    const val BOOK_TITLE = "ItemStats"
}