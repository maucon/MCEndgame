package de.fuballer.mcendgame.util.extension

import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeInstance

object AttributeInstanceExtension {
    fun AttributeInstance.findModifierByKey(key: NamespacedKey) =
        modifiers.find { it.key == key }
}