package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object ArrowfallItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("arrowfall")
    override val customName = "Arrowfall"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.ADDITIONAL_ARROWS, DoubleBounds(0.3, 0.85)),
        RollableCustomAttribute(VanillaAttributeTypes.MOVEMENT_SPEED, DoubleBounds(0.003, 0.01)),
        RollableCustomAttribute(CustomAttributeTypes.DODGE_CHANCE, DoubleBounds(0.05, 0.15)),
    )
}