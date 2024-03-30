package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.util.PluginUtil

object HeadhuntersHaremType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("headhunters_harem")
    override val customName = "Headhunter's Harem"
    override val equipment = Leggings.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.EFFECT_GAIN, 0.075, 0.15),
        RollableAttribute(AttributeType.MORE_DAMAGE_AGAINST_FULL_LIFE, 0.15, 0.35),
    )
}