package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object TyrantsReachItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("tyrants_reach")
    override val customName = "Tyrant's Reach"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(2.0, 4.0)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_RANGE, DoubleBounds(0.2, 0.5)),
    )
}