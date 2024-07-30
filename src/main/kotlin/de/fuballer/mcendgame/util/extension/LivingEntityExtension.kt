package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttributeType
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.event.EntityHealEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object LivingEntityExtension {
    fun LivingEntity.heal(amount: Double) {
        val event = EntityHealEvent(this, amount)
        EventGateway.apply(event)
    }

    fun LivingEntity.getCustomAttributes(): Map<CustomAttributeType, List<CustomAttribute>> {
        return getEntityCustomAttributes(this)
    }

    private fun getEntityCustomAttributes(entity: LivingEntity): Map<CustomAttributeType, List<CustomAttribute>> {
        val attributes = mutableListOf<CustomAttribute>()
        val equipment = entity.equipment ?: return mapOf()

        val helmetAttributes = getValidItemRolledAttributes(equipment.helmet, EquipmentSlot.HEAD)
        attributes.addAll(helmetAttributes)
        val chestplateAttributes = getValidItemRolledAttributes(equipment.chestplate, EquipmentSlot.CHEST)
        attributes.addAll(chestplateAttributes)
        val leggingsAttributes = getValidItemRolledAttributes(equipment.leggings, EquipmentSlot.LEGS)
        attributes.addAll(leggingsAttributes)
        val bootsAttributes = getValidItemRolledAttributes(equipment.boots, EquipmentSlot.FEET)
        attributes.addAll(bootsAttributes)

        val mainHandAttributes = getValidItemRolledAttributes(equipment.itemInMainHand, EquipmentSlot.HAND)
        attributes.addAll(mainHandAttributes)
        val offHandAttributes = getValidItemRolledAttributes(equipment.itemInOffHand, EquipmentSlot.OFF_HAND)
        attributes.addAll(offHandAttributes)

        return attributes
            .filter { it.type is CustomAttributeType }
            .groupBy { it.type as CustomAttributeType }
    }

    private fun getValidItemRolledAttributes(item: ItemStack?, slot: EquipmentSlot): List<CustomAttribute> {
        if (item == null) return listOf()

        val grantAttributes = Equipment.fromMaterial(item.type)
            ?.let { it.slot == slot || !it.slotDependentAttributes } ?: false

        if (!grantAttributes) return listOf()
        return item.getCustomAttributes() ?: listOf()
    }
}