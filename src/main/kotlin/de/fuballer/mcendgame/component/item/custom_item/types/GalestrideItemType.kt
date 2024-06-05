package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object GalestrideItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("galestride")
    override val customName = "Galestride"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.MOVEMENT_SPEED, 0.01, 0.02),
        RollableAttribute(AttributeType.MAX_HEALTH, 1.0, 3.0),
    )
}