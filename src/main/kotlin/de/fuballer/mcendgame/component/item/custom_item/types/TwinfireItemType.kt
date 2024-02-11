package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Sword
import de.fuballer.mcendgame.util.PluginUtil

object TwinfireItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("twinfire")
    override val customName = "Twinfire"
    override val equipment = Sword.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.TWINFIRE_DUAL_WIELD, 0.15, 0.25),
    )
}