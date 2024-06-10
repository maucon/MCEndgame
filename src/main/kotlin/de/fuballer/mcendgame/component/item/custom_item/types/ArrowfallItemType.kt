package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object ArrowfallItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("arrowfall")
    override val customName = "Arrowfall"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ADDITIONAL_ARROWS, 0.3, 0.85),
        RollableAttribute(AttributeType.MOVEMENT_SPEED, 0.003, 0.01),
        RollableAttribute(AttributeType.DODGE_CHANCE, 0.05, 0.15),
    )
}