package de.fuballer.mcendgame.component.crafting.transfiguration

import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setTransfiguration
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object TransfigurationSettings {
    private val BASE_ITEM = Material.PAPER
    private val ITEM_NAME = ChatColor.DARK_GREEN.toString() + "Orb of Transfiguration"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Shuffle an item's attribute rolls")

    const val SHUFFLE_TRIES = 10

    private val TRANSFIGURATION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setTransfiguration()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getTransfigurationItem() = TRANSFIGURATION_ITEM.clone()
}