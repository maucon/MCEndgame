package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Tool
import de.fuballer.mcendgame.util.PluginUtil

object StormfeatherItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("stormfeather")
    override val customName = "Stormfeather"
    override val equipment = Tool.BOW
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ADDITIONAL_ARROWS, 0.3, 0.65),
        RollableAttribute(AttributeType.INCREASED_PROJECTILE_DAMAGE, 0.1, 0.3),
    )
}