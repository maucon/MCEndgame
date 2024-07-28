package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
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
        RollableCustomAttribute(AttributeType.ATTACK_DAMAGE, 2.0, 4.0),
        RollableCustomAttribute(AttributeType.INCREASED_DAMAGE, 0.1, 0.15),
        RollableCustomAttribute(AttributeType.ATTACK_RANGE, 0.2, 0.5),
    )
}