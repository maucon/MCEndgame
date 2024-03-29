package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Sword
import de.fuballer.mcendgame.util.PluginUtil

object BitterfrostItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("bitterfrost")
    override val customName = "Bitterfrost"
    override val equipment = Sword.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.SLOW_ON_HIT, 2.5, 5.0),
        RollableAttribute(AttributeType.TAUNT, 0.25, 0.75),
    )
}