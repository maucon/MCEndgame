package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.util.PluginUtil

object MoonshadowItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("moonshadow")
    override val customName = "Moonshadow"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.MOVEMENT_SPEED, 0.005, 0.015),
        RollableAttribute(AttributeType.DODGE_CHANCE, 0.05, 0.15),
    )
}