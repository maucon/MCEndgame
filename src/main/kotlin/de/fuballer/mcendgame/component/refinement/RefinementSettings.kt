package de.fuballer.mcendgame.component.refinement

import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRefinement
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object RefinementSettings {
    private val BASE_ITEM = Material.STICK
    private val ITEM_NAME = ChatColor.DARK_AQUA.toString() + "Orb of Refinement"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Enhances an item's attribute, sacrificing another.")

    fun refineAttributeValue(enhanceRollRange: Double, sacrificedRollPercentage: Double) = enhanceRollRange * 0.25 * sacrificedRollPercentage

    private val REFINEMENT_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setRefinement()
        setUnmodifiable()
    }

    fun getRefinementItem() = REFINEMENT_ITEM.clone()
}