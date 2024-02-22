package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.util.AttributeUtil
import org.bukkit.attribute.AttributeInstance

object AttributeInstanceExtension {
    fun AttributeInstance.findModifierByName(name: String) =
        AttributeUtil.findModifierByName(this, name)
}