package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object BowItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("archery_annex")
    override val customName = "Archery Annex"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ATTACK_DAMAGE, 4.0, 8.0),
        RollableAttribute(AttributeType.DISABLE_MELEE),
    )
}