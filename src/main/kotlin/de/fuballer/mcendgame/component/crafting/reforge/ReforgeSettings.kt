package de.fuballer.mcendgame.component.crafting.reforge

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setReforgeCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

object ReforgeSettings {
    private val ITEM_NAME = "${ChatColor.LIGHT_PURPLE}Reforge Stone"
    private val ITEM_LORE = listOf("${ChatColor.WHITE}Forge a new custom item from another.", *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    private val ITEM = ItemCreatorUtil.create(
        ItemStack(CraftingItemSettings.BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setReforgeCraftingItem()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getItem() = ITEM.clone()
}