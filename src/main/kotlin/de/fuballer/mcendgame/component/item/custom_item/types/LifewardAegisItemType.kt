package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Tool
import de.fuballer.mcendgame.util.PluginUtil

object LifewardAegisItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("lifeward_aegis")
    override val customName = "Lifeward Aegis"
    override val equipment = Tool.SHIELD
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.HEAL_ON_BLOCK, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(-4.0, -2.0)),
    )
}