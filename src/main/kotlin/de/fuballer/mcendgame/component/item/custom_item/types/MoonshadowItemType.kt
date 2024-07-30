package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.util.PluginUtil

object MoonshadowItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("moonshadow")
    override val customName = "Moonshadow"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MOVEMENT_SPEED, DoubleBounds(0.005, 0.015)),
        RollableCustomAttribute(CustomAttributeTypes.DODGE_CHANCE, DoubleBounds(0.05, 0.15)),
    )
}