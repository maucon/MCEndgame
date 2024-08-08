package de.fuballer.mcendgame.component.crafting.roll_shuffle

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.util.ItemCreator
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRollShuffleCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack

object RollShuffleSettings {
    private val ITEM_NAME = TextComponent.create("Orb of Transfiguration", NamedTextColor.DARK_GREEN)
    private val ITEM_LORE = listOf(TextComponent.create("Shuffle an items's attribute rolls"), *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)

    const val SHUFFLE_TRIES = 10

    private val ITEM = ItemCreator.create(
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