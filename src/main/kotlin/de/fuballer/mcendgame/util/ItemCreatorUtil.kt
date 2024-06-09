package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomItemType
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object ItemCreatorUtil {
    fun create(
        itemStack: ItemStack,
        itemName: String?,
        itemLore: List<String>? = listOf()
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

    fun createCustomItem(
        itemType: CustomItemType,
        percentageRoll: Double = Random.nextDouble()
    ): ItemStack {
        val item = ItemStack(itemType.equipment.material)
        val itemMeta = item.itemMeta!!

        itemMeta.setDisplayName("${ChatColor.GOLD}${itemType.customName}")
        val customAttributes = itemType.attributes
            .map { it.roll(percentageRoll) }

        if (!itemType.usesEquipmentBaseStats) {
            itemMeta.attributeModifiers?.let {
                it.forEach { attribute, _ -> itemMeta.removeAttributeModifier(attribute) }
            }
        }

        item.itemMeta = itemMeta

        item.setCustomItemType(itemType)
        item.setCustomAttributes(customAttributes)

        ItemUtil.updateAttributesAndLore(item)
        return item
    }
}
