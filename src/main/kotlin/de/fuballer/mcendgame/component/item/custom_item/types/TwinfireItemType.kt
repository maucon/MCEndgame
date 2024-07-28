package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Sword
import de.fuballer.mcendgame.util.PluginUtil

object TwinfireItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("twinfire")
    override val customName = "Twinfire"
    override val equipment = Sword.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(AttributeType.TWINFIRE_DUAL_WIELD, 0.15, 0.25),
        RollableCustomAttribute(AttributeType.ATTACK_SPEED, 0.1, 0.15),
    )
}