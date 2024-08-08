package de.fuballer.mcendgame.component.crafting.duplication

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreator
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setDuplicationCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack

object DuplicationSettings {
    private val ITEM_NAME = TextComponent.create("Mirror's Touch", NamedTextColor.GOLD)
    private val ITEM_LORE = listOf(TextComponent.create("Duplicate any item"), *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    private val ITEM = ItemCreator.create(
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