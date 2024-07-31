package de.fuballer.mcendgame.component.crafting.roll_sacrifice

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRollSacrificeCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

object RollSacrificeSettings {
    private val ITEM_NAME = "${ChatColor.DARK_AQUA}Orb of Refinement"
    private val ITEM_LORE = listOf("${ChatColor.WHITE}Enhance an item's attribute by sacrificing another.", *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    const val REFINEMENT_ATTRIBUTE_VALUE = 0.5

    private val ITEM = ItemCreatorUtil.create(
        ItemStack(CraftingItemSettings.BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setRollSacrificeCraftingItem()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getItem() = ITEM.clone()
}