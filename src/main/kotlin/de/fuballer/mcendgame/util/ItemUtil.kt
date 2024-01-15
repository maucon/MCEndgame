package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.persistent_data.DataTypeKeys
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object ItemUtil {
    fun addItemAttribute(
        itemMeta: ItemMeta,
        attribute: Attribute,
        value: Double,
        operation: AttributeModifier.Operation
    ) {
        itemMeta.addAttributeModifier(
            attribute,
            AttributeModifier(
                attribute.key.key,
                value,
                operation
            )
        )
    }

    fun isCorrupted(item: ItemStack): Boolean {
        val itemMeta = item.itemMeta ?: return false

        return PersistentDataUtil.getBooleanValue(itemMeta, DataTypeKeys.CORRUPTED)
    }

    fun setCorrupted(item: ItemStack, corrupted: Boolean = true) {
        val newItemMeta = item.itemMeta ?: return

        PersistentDataUtil.setValue(newItemMeta, DataTypeKeys.CORRUPTED, corrupted)
        item.itemMeta = newItemMeta
    }
}