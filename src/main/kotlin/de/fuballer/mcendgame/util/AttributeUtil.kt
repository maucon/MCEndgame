package de.fuballer.mcendgame.util

import org.bukkit.attribute.AttributeInstance

object AttributeUtil {
    fun findModifierByName(attribute: AttributeInstance, name: String) =
        attribute.modifiers.find { it.name == name }
}