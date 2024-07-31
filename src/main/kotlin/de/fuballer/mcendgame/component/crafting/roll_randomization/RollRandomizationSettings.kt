package de.fuballer.mcendgame.component.crafting.roll_randomization

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRollRandomizationCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

object RollRandomizationSettings {
    private val ITEM_NAME = "${ChatColor.AQUA}Gambler's Gem"
    private val ITEM_LORE = listOf("${ChatColor.WHITE}Randomize all values of an item's attributes.", *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    private val ITEM = ItemCreatorUtil.create(
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