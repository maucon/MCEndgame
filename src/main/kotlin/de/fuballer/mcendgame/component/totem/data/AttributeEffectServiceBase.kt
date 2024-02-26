package de.fuballer.mcendgame.component.totem.data

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.util.extension.AttributeInstanceExtension.findModifierByName
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

open class AttributeEffectServiceBase(
    private val totemType: TotemType,
    private val attributeType: Attribute,
    private val attributeModifierOperation: Operation
) : Listener {
    private val modifierName = totemType.key.toString()

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val tier = event.player.getHighestTotemTier(totemType) ?: return

        val (value) = totemType.getValues(tier)
        val modifier = AttributeModifier(modifierName, value, attributeModifierOperation)

        val attribute = event.player.getAttribute(attributeType) ?: return
        if (attribute.findModifierByName(modifierName) != null) return

        attribute.addModifier(modifier)
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val attribute = event.player.getAttribute(attributeType) ?: return
        val modifier = attribute.findModifierByName(modifierName) ?: return

        attribute.removeModifier(modifier)
    }
}