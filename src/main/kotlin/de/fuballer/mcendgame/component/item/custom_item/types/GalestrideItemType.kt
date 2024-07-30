package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object GalestrideItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("galestride")
    override val customName = "Galestride"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MOVEMENT_SPEED, DoubleBounds(0.01, 0.02)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.05, 0.10)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, DoubleBounds(1.0, 3.0)),
    )
}