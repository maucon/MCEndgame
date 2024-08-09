package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.IntBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.StringBounds
import de.fuballer.mcendgame.component.item.attribute.effects.summon_support_wolf.SupportWolfType
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object PackleaderItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("packleader")
    override val customName = "Packleader"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.SUMMON_SUPPORT_WOLF, StringBounds(SupportWolfType.getAsStringList()), IntBounds(1, 4)),
        RollableCustomAttribute(CustomAttributeTypes.SUMMON_SUPPORT_WOLF, StringBounds(SupportWolfType.getAsStringList()), IntBounds(1, 4)),
    )
}