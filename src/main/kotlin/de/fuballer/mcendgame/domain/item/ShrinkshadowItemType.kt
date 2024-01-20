package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.armor.Leggings
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