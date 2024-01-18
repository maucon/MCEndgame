package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.armor.Boots

object GeistergaloschenItemType : CustomItemType {
    override val customName = "Geistergaloschen"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.HEALTH_SCALED_SIZE, 10.0, 20.0),
        RollableAttribute(AttributeType.MAX_HEALTH, 2.0, 4.0),
        RollableAttribute(AttributeType.HEALTH_SCALED_SPEED, 10.0, 20.0)
    )
}