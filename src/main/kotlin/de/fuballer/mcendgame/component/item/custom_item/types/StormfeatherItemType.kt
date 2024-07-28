package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Tool
import de.fuballer.mcendgame.util.PluginUtil

object StormfeatherItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("stormfeather")
    override val customName = "Stormfeather"
    override val equipment = Tool.BOW
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(AttributeType.ADDITIONAL_ARROWS, 0.35, 0.75),
        RollableCustomAttribute(AttributeType.INCREASED_PROJECTILE_DAMAGE, 0.1, 0.35),
    )
}