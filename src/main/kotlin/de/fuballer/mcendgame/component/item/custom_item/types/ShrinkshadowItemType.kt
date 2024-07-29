package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object ShrinkshadowItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("shrinkshadow")
    override val customName = "Shrinkshadow"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.DODGE_CHANCE, DoubleBounds(0.2, 0.35)),
        RollableCustomAttribute(CustomAttributeTypes.HEALTH_RESERVATION, DoubleBounds(0.2, 0.4)),
        RollableCustomAttribute(VanillaAttributeTypes.SIZE_INCREASE, DoubleBounds(-0.2, -0.1)),
    )
}