package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.util.PluginUtil

object GeistergaloschenItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("geistergaloschen")
    override val customName = "Geistergaloschen"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.HEALTH_SCALED_SIZE, 10.0, 20.0),
        RollableAttribute(AttributeType.MAX_HEALTH, 2.0, 4.0),
    )
}