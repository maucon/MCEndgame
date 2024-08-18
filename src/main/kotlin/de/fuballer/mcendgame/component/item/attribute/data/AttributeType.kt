package de.fuballer.mcendgame.component.item.attribute.data

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

sealed class AttributeType(
    val lore: (List<AttributeRoll<*>>) -> String
)

class VanillaAttributeType(
    val attribute: Attribute,
    val scaleType: AttributeModifier.Operation,
    lore: (List<AttributeRoll<*>>) -> String
) : AttributeType(lore)

class CustomAttributeType(
    lore: (List<AttributeRoll<*>>) -> String
) : AttributeType(lore)