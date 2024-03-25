package de.fuballer.mcendgame.component.crafting.imitation

import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setImitation
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ImitationSettings {
    private val BASE_ITEM = Material.STICK
    private val ITEM_NAME = ChatColor.WHITE.toString() + "Orb of Imitation"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Duplicate an item")

    private val IMITATION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setImitation()
        setCraftingItem()
        setUnmodifiable()
    }

    fun getImitationItem() = IMITATION_ITEM.clone()
}