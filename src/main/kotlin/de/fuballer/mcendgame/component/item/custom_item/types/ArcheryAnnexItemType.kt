package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object ArcheryAnnexItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("archery_annex")
    override val customName = "Archery Annex"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.INCREASED_PROJECTILE_DAMAGE, 0.5, 1.5),
        RollableAttribute(AttributeType.DISABLE_MELEE),
    )
}