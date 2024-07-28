package de.fuballer.mcendgame.component.crafting.refinement

import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRefinement
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object RefinementSettings {
    private val BASE_ITEM = Material.PAPER
    private val ITEM_NAME = ChatColor.DARK_AQUA.toString() + "Orb of Refinement"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Enhance an item's attribute by sacrificing another")

    const val REFINEMENT_ATTRIBUTE_VALUE = 0.5

    private val REFINEMENT_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setRefinement()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getRefinementItem() = REFINEMENT_ITEM.clone()
}