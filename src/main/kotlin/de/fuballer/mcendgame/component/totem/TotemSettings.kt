package de.fuballer.mcendgame.component.totem

import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType

object TotemSettings {
    const val COMMAND_NAME = "dungeon-totems"
    const val GIVE_COMMAND_NAME = "give-dungeon-totem"

    val TOTEM_BASE_TYPE = Material.TOTEM_OF_UNDYING
    val TOTEM_ITEM_DUNGEON_DISCLAIMER = listOf(
        TextComponent.empty(),
        TextComponent.create("The totem's effect is only active", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC),
        TextComponent.create("when in the totem inventory and", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC),
        TextComponent.create("whilst inside a dungeon.", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC)
    )

    val TOTEM_WINDOW_TYPE = InventoryType.HOPPER
    val TOTEM_WINDOW_SIZE = TOTEM_WINDOW_TYPE.defaultSize
    val TOTEM_WINDOW_TITLE = TextComponent.create("Totems")
    val CANNOT_CHANGE_TOTEM_MESSAGE = TextComponent.error("You cannot change totems whilst inside a dungeon")

    val LORE_COLOR: NamedTextColor = NamedTextColor.DARK_GREEN
}