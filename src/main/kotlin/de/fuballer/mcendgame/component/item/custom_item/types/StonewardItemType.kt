package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object StonewardItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("stoneward")
    override val customName = "Stoneward"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MOVEMENT_SPEED, DoubleBounds(-0.025, -0.015)),
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR, DoubleBounds(2.0, 3.0)),
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, DoubleBounds(2.5, 3.5)),
        RollableCustomAttribute(VanillaAttributeTypes.SIZE_INCREASE, DoubleBounds(0.05, 0.12)),
    )
}