package de.fuballer.mcendgame.component.item.attribute.data

import de.fuballer.mcendgame.component.item.attribute.AttributeType

data class BaseAttribute(
    val type: AttributeType,
    var roll: Double
)