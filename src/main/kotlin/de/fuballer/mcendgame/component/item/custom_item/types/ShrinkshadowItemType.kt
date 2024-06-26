package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object ShrinkshadowItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("shrinkshadow")
    override val customName = "Shrinkshadow"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.DODGE_CHANCE, 0.2, 0.35),
        RollableAttribute(AttributeType.HEALTH_RESERVATION, 0.2, 0.4),
        RollableAttribute(AttributeType.SIZE_INCREASE, -0.2, -0.1),
    )
}