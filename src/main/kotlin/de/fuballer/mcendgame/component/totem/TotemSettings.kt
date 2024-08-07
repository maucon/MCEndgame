package de.fuballer.mcendgame.component.totem

import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import java.text.DecimalFormat

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

    private val LORE_NUMBER_FORMAT = DecimalFormat("0.#")
    private val LORE_COLOR = NamedTextColor.DARK_GREEN

    fun formatLore(loreFormat: String, values: List<Double>): List<Component> {
        val formattedValues = values.map { LORE_NUMBER_FORMAT.format(it) }
        val lore = String.format(loreFormat, *formattedValues.toTypedArray())

        return lore.split("\\")
            .map { TextComponent.create(it, LORE_COLOR) }
    }
}