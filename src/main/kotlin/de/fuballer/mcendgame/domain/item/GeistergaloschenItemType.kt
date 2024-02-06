package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.armor.Boots
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