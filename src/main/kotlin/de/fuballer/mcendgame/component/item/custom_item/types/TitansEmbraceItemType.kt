package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object TitansEmbraceItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("titans_embrace")
    override val customName = "Titan's Embrace"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = false
    override val attributes = listOf(
        RollableAttribute(AttributeType.SIZE_INCREASE, 0.1, 0.25),
        RollableAttribute(AttributeType.MAX_HEALTH, 3.0, 6.0),
        RollableAttribute(AttributeType.MAX_HEALTH_INCREASE, 0.1, 0.2),
    )
}