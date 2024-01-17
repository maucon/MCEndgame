package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.armor.Boots
import de.fuballer.mcendgame.domain.equipment.armor.Chestplate
import de.fuballer.mcendgame.domain.equipment.tool.Sword

object TwinfireItemType : CustomItemType {
    override val customName = "Twinfire"
    override val equipment = Sword.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.TWINFIRE_DUAL_WIELD, 0.2, 0.3),
    )
}