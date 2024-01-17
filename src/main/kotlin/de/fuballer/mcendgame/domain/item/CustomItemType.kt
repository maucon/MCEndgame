package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment

interface CustomItemType {
    val customName: String
    val equipment: Equipment
    val usesEquipmentBaseStats: Boolean
    val attributes: List<RollableAttribute>
}