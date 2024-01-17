package de.fuballer.mcendgame.util

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object ItemCreatorUtil {
    fun create(
        itemStack: ItemStack,
        itemName: String?,
        itemLore: List<String>?
    ): ItemStack {
        val meta = itemStack.itemMeta!!
        meta.setDisplayName(itemName)
        meta.lore = itemLore
        itemStack.itemMeta = meta
        return itemStack
    }

    fun create(
        itemStack: ItemStack,
        itemName: String,
        itemLore: List<String>?,
        enchantment: Enchantment,
        enchantmentLevel: Int,
        enchantmentIgnoreLevelRestriction: Boolean,
        itemFlag: ItemFlag
    ): ItemStack {
        val meta = itemStack.itemMeta!!
        meta.setDisplayName(itemName)
        meta.lore = itemLore
        meta.addEnchant(enchantment, enchantmentLevel, enchantmentIgnoreLevelRestriction)
        meta.addItemFlags(itemFlag)
        itemStack.itemMeta = meta
        return itemStack
    }
}
