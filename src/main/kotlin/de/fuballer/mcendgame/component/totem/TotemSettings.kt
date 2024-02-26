package de.fuballer.mcendgame.component.totem

import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import java.text.DecimalFormat

object TotemSettings {
    const val COMMAND_NAME = "dungeon-totems"
    const val GIVE_COMMAND_NAME = "dungeon-totem-give"

    val TOTEM_BASE_TYPE = Material.TOTEM_OF_UNDYING

    val TOTEM_WINDOW_TYPE = InventoryType.HOPPER
    val TOTEM_WINDOW_SIZE = TOTEM_WINDOW_TYPE.defaultSize
    const val TOTEM_WINDOW_TITLE = "Totems"
    val CANNOT_CHANGE_TOTEM_MESSAGE = "${ChatColor.RED}You cannot change totems whilst inside a dungeon."

    private val LORE_NUMBER_FORMAT = DecimalFormat("0.#")
    private val LORE_COLOR = "${ChatColor.GRAY}${ChatColor.ITALIC}"

    const val TOTEM_DROP_CHANCE = 0.00133

    val TOTEM_TIERS = listOf(
        SortableRandomOption(1000, 0, TotemTier.COMMON),
        SortableRandomOption(200, 1, TotemTier.UNCOMMON),
        SortableRandomOption(25, 2, TotemTier.RARE),
        SortableRandomOption(3, 3, TotemTier.LEGENDARY)
    )

    val TOTEM_TYPES = TotemType.REGISTRY
        .map { RandomOption(1, it.value) }

    fun formatLore(loreFormat: String, values: List<Double>): List<String> {
        val formattedValues = values.map { LORE_NUMBER_FORMAT.format(it) }
        val lore = String.format(loreFormat, *formattedValues.toTypedArray())

        return lore.split("\\")
            .map { "$LORE_COLOR$it" }
    }
}