package de.fuballer.mcendgame.component.totem.data

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.util.extension.AttributeInstanceExtension.findModifierByKey
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlotGroup

open class AttributeEffectServiceBase(
    private val totemType: TotemType,
    private val attributeType: Attribute,
    private val attributeModifierOperation: Operation
) : Listener {
    private val modifierKey = totemType.key

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val tier = event.player.getHighestTotemTier(totemType) ?: return

        val (value) = totemType.getValues(tier)
        val modifier = AttributeModifier(modifierKey, value, attributeModifierOperation, EquipmentSlotGroup.ANY)

        val attribute = event.player.getAttribute(attributeType) ?: return
        if (attribute.findModifierByKey(modifierKey) != null) return

        attribute.addModifier(modifier)
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val attribute = event.player.getAttribute(attributeType) ?: return
        val modifier = attribute.findModifierByKey(modifierKey) ?: return

        attribute.removeModifier(modifier)
    }
}