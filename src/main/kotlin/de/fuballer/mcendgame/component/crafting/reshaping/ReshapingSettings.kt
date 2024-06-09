package de.fuballer.mcendgame.component.crafting.reshaping

import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setReshaping
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ReshapingSettings {
    private val BASE_ITEM = Material.STICK
    private val ITEM_NAME = ChatColor.LIGHT_PURPLE.toString() + "Orb of Reshaping"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Forge a new custom item from another")

    private val RESHAPING_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setReshaping()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getReshapingItem() = RESHAPING_ITEM.clone()
}