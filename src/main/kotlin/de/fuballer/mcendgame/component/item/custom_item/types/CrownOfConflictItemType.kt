package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object CrownOfConflictItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("crown_of_conflict")
    override val customName = "Crown of Conflict"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.HEALTH_RESERVATION, DoubleBounds(0.15, 0.3)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH_INCREASE, DoubleBounds(0.1, 0.2)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, DoubleBounds(-4.0, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.SIZE_INCREASE, DoubleBounds(-0.05, 0.05)),
    )
}