package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object StonewardItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("stoneward")
    override val customName = "Stoneward"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.MOVEMENT_SPEED, -0.025, -0.015),
        RollableAttribute(AttributeType.ARMOR, 2.0, 3.0),
        RollableAttribute(AttributeType.ARMOR_TOUGHNESS, 2.5, 4.0),
        RollableAttribute(AttributeType.SIZE_INCREASE, 0.05, 0.15),
    )
}