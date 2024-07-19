package de.fuballer.mcendgame.component.totem

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import java.text.DecimalFormat

object TotemSettings {
    const val COMMAND_NAME = "dungeon-totems"
    const val GIVE_COMMAND_NAME = "give-dungeon-totem"

    val TOTEM_BASE_TYPE = Material.TOTEM_OF_UNDYING
    val TOTEM_ITEM_DUNGEON_DISCLAIMER = listOf(
        "",
        "${ChatColor.GRAY}${ChatColor.ITALIC}The totem's effect is only active",
        "${ChatColor.GRAY}${ChatColor.ITALIC}when in the totem inventory and",
        "${ChatColor.GRAY}${ChatColor.ITALIC}whilst inside a dungeon."
    )

    val TOTEM_WINDOW_TYPE = InventoryType.HOPPER
    val TOTEM_WINDOW_SIZE = TOTEM_WINDOW_TYPE.defaultSize
    const val TOTEM_WINDOW_TITLE = "Totems"
    val CANNOT_CHANGE_TOTEM_MESSAGE = "${ChatColor.RED}You cannot change totems whilst inside a dungeon"

    private val LORE_NUMBER_FORMAT = DecimalFormat("0.#")
    private val LORE_COLOR = "${ChatColor.DARK_GREEN}"

    fun formatLore(loreFormat: String, values: List<Double>): List<String> {
        val formattedValues = values.map { LORE_NUMBER_FORMAT.format(it) }
        val lore = String.format(loreFormat, *formattedValues.toTypedArray())

        return lore.split("\\")
            .map { "$LORE_COLOR$it" }
    }
}