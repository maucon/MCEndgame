package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object CrownOfConflictItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("crown_of_conflict")
    override val customName = "Crown of Conflict"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.HEALTH_RESERVATION, 0.15, 0.3),
        RollableAttribute(AttributeType.MAX_HEALTH_INCREASE, 0.1, 0.2),
        RollableAttribute(AttributeType.MAX_HEALTH, -3.0, 3.0),
        RollableAttribute(AttributeType.SIZE_INCREASE, -0.1, 0.1),
    )
}