package de.fuballer.mcendgame.component.crafting.roll_shuffle

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRollShuffleCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

object RollShuffleSettings {
    private val ITEM_NAME = "${ChatColor.DARK_GREEN}Orb of Transfiguration"
    private val ITEM_LORE = listOf("${ChatColor.WHITE}Shuffle an item's attribute rolls.", *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    const val SHUFFLE_TRIES = 10

    private val ITEM = ItemCreatorUtil.create(
        ItemStack(CraftingItemSettings.BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setRollShuffleCraftingItem()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getItem() = ITEM.clone()
}