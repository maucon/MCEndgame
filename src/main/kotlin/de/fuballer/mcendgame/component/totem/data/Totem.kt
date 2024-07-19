package de.fuballer.mcendgame.component.totem.data

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setTotem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.inventory.ItemStack

data class Totem(
    var type: TotemType,
    var tier: TotemTier
) {
    fun toItem(): ItemStack {
        val item = ItemStack(TotemSettings.TOTEM_BASE_TYPE)
        val itemMeta = item.itemMeta!!

        itemMeta.setDisplayName("${tier.color}${type.displayName}")

        val lore = mutableListOf<String>()
        lore.addAll(type.getLore(tier))
        lore.addAll(TotemSettings.TOTEM_ITEM_DUNGEON_DISCLAIMER)

        itemMeta.lore = lore
        item.itemMeta = itemMeta

        item.setTotem(this)
        item.setUnmodifiable()

        return item
    }
}