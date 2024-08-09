package de.fuballer.mcendgame.util

import net.kyori.adventure.text.Component
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object ItemCreator {
    fun create(
        itemStack: ItemStack,
        itemName: Component?,
        itemLore: List<Component>? = listOf()
    ): ItemStack {
        val meta = itemStack.itemMeta!!
        meta.displayName(itemName)
        meta.lore(itemLore)
        itemStack.itemMeta = meta
        return itemStack
    }

    fun create(
        itemStack: ItemStack,
        itemName: Component?,
        itemLore: List<Component>?,
        enchantment: Enchantment,
        enchantmentLevel: Int,
        enchantmentIgnoreLevelRestriction: Boolean,
        itemFlag: ItemFlag
    ): ItemStack {
        val meta = itemStack.itemMeta!!
        meta.displayName(itemName)
        meta.lore(itemLore)
        meta.addEnchant(enchantment, enchantmentLevel, enchantmentIgnoreLevelRestriction)
        meta.addItemFlags(itemFlag)
        itemStack.itemMeta = meta
        return itemStack
    }
}