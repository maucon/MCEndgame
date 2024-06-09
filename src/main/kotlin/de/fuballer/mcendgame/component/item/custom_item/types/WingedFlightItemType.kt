package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.util.PluginUtil

object WingedFlightItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("winged_flight")
    override val customName = "Winged Flight"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = false
    override val attributes = listOf(
        RollableAttribute(AttributeType.MOVEMENT_SPEED, 0.02, 0.035),
    )
}