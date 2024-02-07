package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object ShrinkshadowItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("shrinkshadow")
    override val customName = "Shrinkshadow"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.DODGE_CHANCE, 0.2, 0.3),
        RollableAttribute(AttributeType.MAX_HEALTH, -12.0, -8.0),
        RollableAttribute(AttributeType.SIZE_INCREASE, -0.25, -0.1),
    )
}