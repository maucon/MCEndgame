package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object TitansEmbraceItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("titans_embrace")
    override val customName = "Titan's Embrace"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = false
    override val attributes = listOf(
        RollableAttribute(AttributeType.HEALTH_SCALED_SIZE, 5.0, 10.0),
        RollableAttribute(AttributeType.MAX_HEALTH, 3.0, 6.0),
        RollableAttribute(AttributeType.MAX_HEALTH_INCREASE, 0.1, 0.2),
    )
}