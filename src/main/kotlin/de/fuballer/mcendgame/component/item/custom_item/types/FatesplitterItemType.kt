package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Axe
import de.fuballer.mcendgame.util.PluginUtil

object FatesplitterItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("fatesplitter")
    override val customName = "Fatesplitter"
    override val equipment = Axe.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(AttributeType.INCREASED_DAMAGE_PER_MISSING_HEART, 0.05, 0.1),
        RollableCustomAttribute(AttributeType.ATTACK_DAMAGE, 2.0, 5.0),
        RollableCustomAttribute(AttributeType.ATTACK_SPEED, -0.2, -0.1),
    )
}