package de.fuballer.mcendgame.util

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.meta.ItemMeta

object ItemUtil {
    fun addItemModifier(
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
}