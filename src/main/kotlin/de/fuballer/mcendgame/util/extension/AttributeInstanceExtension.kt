package de.fuballer.mcendgame.util.extension

import org.bukkit.attribute.AttributeInstance

object AttributeInstanceExtension {
    fun AttributeInstance.findModifierByName(name: String) =
        modifiers.find { it.name == name }
}