package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object ArcheryAnnexItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("archery_annex")
    override val customName = "Archery Annex"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ATTACK_DAMAGE, 4.0, 8.0),
        RollableAttribute(AttributeType.DISABLE_MELEE),
    )
}