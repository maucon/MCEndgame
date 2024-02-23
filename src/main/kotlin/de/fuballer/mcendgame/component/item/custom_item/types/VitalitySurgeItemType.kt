package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object VitalitySurgeItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("vitality_surge")
    override val customName = "Vitality Surge"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ABSORPTION_ON_HIGH_DAMAGE_TAKEN, 5.0, 8.0),
    )
}