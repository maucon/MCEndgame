package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.util.PluginUtil

object GeistergaloschenItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("geistergaloschen")
    override val customName = "Geistergaloschen"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(AttributeType.BACKSTAB, 0.2, 0.4),
        RollableCustomAttribute(AttributeType.STEALTH),
        RollableCustomAttribute(AttributeType.MOVEMENT_SPEED, 0.005, 0.015),
    )
}