package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.RollType
import de.fuballer.mcendgame.component.item.attribute.data.SingleValueAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object LivingEntityExtension {
    fun LivingEntity.heal(
        amount: Double,
        reason: RegainReason = RegainReason.CUSTOM
    ) {
        val event = EntityRegainHealthEvent(this, amount, reason)
        EventGateway.apply(event)
    }

    fun LivingEntity.getCustomAttributes(): Map<AttributeType, List<Double>> {
        return getEntityCustomAttributes(this)
    }

    private fun getEntityCustomAttributes(entity: LivingEntity): Map<AttributeType, List<Double>> {
        val attributes = mutableListOf<CustomAttribute>()
        val equipment = entity.equipment ?: return mapOf()

        val helmetAttributes = getValidItemAttributes(equipment.helmet, EquipmentSlot.HEAD)
        attributes.addAll(helmetAttributes)
        val chestplateAttributes = getValidItemAttributes(equipment.chestplate, EquipmentSlot.CHEST)
        attributes.addAll(chestplateAttributes)
        val leggingsAttributes = getValidItemAttributes(equipment.leggings, EquipmentSlot.LEGS)
        attributes.addAll(leggingsAttributes)
        val bootsAttributes = getValidItemAttributes(equipment.boots, EquipmentSlot.FEET)
        attributes.addAll(bootsAttributes)

        val mainHandAttributes = getValidItemAttributes(equipment.itemInMainHand, EquipmentSlot.HAND)
        attributes.addAll(mainHandAttributes)
        val offHandAttributes = getValidItemAttributes(equipment.itemInOffHand, EquipmentSlot.OFF_HAND)
        attributes.addAll(offHandAttributes)

        return attributes
            .filter { !it.type.isVanillaAttributeType }
            .groupBy { it.type }
            .mapValues { (_, attributes) ->
                attributes.map { attribute ->
                    when (attribute.rollType) {
                        RollType.STATIC -> 0.0
                        RollType.SINGLE -> (attribute as SingleValueAttribute).getAbsoluteRoll()
                    }
                }
            }
    }

    private fun getValidItemAttributes(item: ItemStack?, slot: EquipmentSlot): List<CustomAttribute> {
        if (item == null) return listOf()

        val grantAttributes = Equipment.fromMaterial(item.type)
            ?.let { it.slot == slot || !it.slotDependentAttributes } ?: false

        if (!grantAttributes) return listOf()
        return item.getCustomAttributes() ?: listOf()
    }
}