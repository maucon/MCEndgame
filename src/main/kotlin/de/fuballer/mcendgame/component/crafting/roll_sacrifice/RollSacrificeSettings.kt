package de.fuballer.mcendgame.component.crafting.roll_sacrifice

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreator
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRollSacrificeCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack

object RollSacrificeSettings {
    private val ITEM_NAME = TextComponent.create("Orb of Refinement", NamedTextColor.DARK_AQUA)
    private val ITEM_LORE = listOf(TextComponent.create("Enhance an item's attribute by sacrificing another"), *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    const val REFINEMENT_ATTRIBUTE_VALUE = 0.5

    private val ITEM = ItemCreator.create(
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