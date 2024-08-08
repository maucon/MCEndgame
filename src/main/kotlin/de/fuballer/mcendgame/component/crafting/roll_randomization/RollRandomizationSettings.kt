package de.fuballer.mcendgame.component.crafting.roll_randomization

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreator
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRollRandomizationCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack

object RollRandomizationSettings {
    private val ITEM_NAME = TextComponent.create("Gambler's Gem", NamedTextColor.AQUA)
    private val ITEM_LORE = listOf(TextComponent.create("Randomize all values of an item's attributes"), *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    private val ITEM = ItemCreator.create(
        ItemStack(CraftingItemSettings.BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setRollRandomizationCraftingItem()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getItem() = ITEM.clone()
}