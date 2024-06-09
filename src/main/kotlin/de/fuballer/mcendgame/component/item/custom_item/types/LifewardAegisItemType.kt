package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Tool
import de.fuballer.mcendgame.util.PluginUtil

object LifewardAegisItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("lifeward_aegis")
    override val customName = "Lifeward Aegis"
    override val equipment = Tool.SHIELD
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.HEAL_ON_BLOCK, 0.75, 2.0),
        RollableAttribute(AttributeType.ATTACK_DAMAGE, -5.0, -2.0),
    )
}