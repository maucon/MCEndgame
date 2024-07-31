package de.fuballer.mcendgame.component.crafting.duplication

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setDuplicationCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

object DuplicationSettings {
    private val ITEM_NAME = "${ChatColor.GOLD}Mirror's Touch"
    private val ITEM_LORE = listOf("${ChatColor.WHITE}Duplicate any item.", *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    private val ITEM = ItemCreatorUtil.create(
        ItemStack(CraftingItemSettings.BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setDuplicationCraftingItem()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getItem() = ITEM.clone()
}