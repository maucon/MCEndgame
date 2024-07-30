package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.util.PluginUtil

object WingedFlightItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("winged_flight")
    override val customName = "Winged Flight"
    override val equipment = Boots.NETHERITE
    override val usesEquipmentBaseStats = false
    override val attributes = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MOVEMENT_SPEED, DoubleBounds(0.02, 0.035)),
    )
}