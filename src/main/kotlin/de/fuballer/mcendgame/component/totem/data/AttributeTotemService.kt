package de.fuballer.mcendgame.component.totem.data

import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.util.extension.AttributeInstanceExtension.findModifierByKey
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.addCustomEntityAttribute
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.removeCustomEntityAttributesBySource
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

open class AttributeTotemService(
    private val totemType: AttributeTotemType,
) : Listener {
    private val totemKey = totemType.key

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = player.getHighestTotemTier(totemType) ?: return

        val attributeType = totemType.attributeType
        val attributeRolls = totemType.getAttributeRollsByTier(tier)

        val customAttribute = CustomAttribute(attributeType, attributeRolls, source = totemKey)
        player.addCustomEntityAttribute(customAttribute)

        if (attributeType is VanillaAttributeType) {
            addVanillaAttribute(player, attributeType, attributeRolls)
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player
        val attributeType = totemType.attributeType

        player.removeCustomEntityAttributesBySource(source = totemKey)

        if (attributeType is VanillaAttributeType) {
            removeVanillaAttribute(player, attributeType)
        }
    }

    private fun addVanillaAttribute(
        player: Player,
        attributeType: VanillaAttributeType,
        attributeRolls: List<AttributeRoll<*>>
    ) {
        val amount = attributeRolls.getFirstAsDouble() // vanilla attributes always have one double roll
        val modifier = AttributeModifier(totemKey, amount, attributeType.scaleType)

        val attribute = player.getAttribute(attributeType.attribute) ?: return
        if (attribute.findModifierByKey(totemKey) != null) return // fail-safe; should only happen on non-graceful server shutdown

        attribute.addModifier(modifier)
    }

    private fun removeVanillaAttribute(
        player: Player,
        attributeType: VanillaAttributeType
    ) {
        val attribute = player.getAttribute(attributeType.attribute) ?: return
        val modifier = attribute.findModifierByKey(totemKey) ?: return

        attribute.removeModifier(modifier)
    }
}