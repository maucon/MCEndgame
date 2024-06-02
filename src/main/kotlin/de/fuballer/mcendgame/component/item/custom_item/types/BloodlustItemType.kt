package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.util.PluginUtil

object BloodlustItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("bloodlust")
    override val customName = "Bloodlust"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.MOVEMENT_SPEED, 0.008, 0.02),
        RollableAttribute(AttributeType.ATTACK_SPEED, 0.1, 0.15),
    )
}