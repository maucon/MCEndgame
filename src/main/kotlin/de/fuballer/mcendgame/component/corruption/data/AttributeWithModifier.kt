package de.fuballer.mcendgame.component.corruption.data

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

data class AttributeWithModifier(
    val attribute: Attribute,
    val modifier: AttributeModifier
)
