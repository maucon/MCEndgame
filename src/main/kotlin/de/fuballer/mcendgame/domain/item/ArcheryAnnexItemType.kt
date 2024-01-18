package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.armor.Boots
import de.fuballer.mcendgame.domain.equipment.armor.Helmet

object ArcheryAnnexItemType : CustomItemType {
    override val customName = "Archery Annex"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ATTACK_DAMAGE, 4.0, 8.0),
        RollableAttribute(AttributeType.DISABLE_MELEE),
    )
}