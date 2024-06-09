package de.fuballer.mcendgame.component.item.attribute.data

import de.fuballer.mcendgame.component.item.attribute.AttributeType

abstract class CustomAttribute(
    val type: AttributeType,
    val rollType: RollType
)

class StaticAttribute(
    type: AttributeType
) : CustomAttribute(type, RollType.STATIC)

class SingleValueAttribute(
    type: AttributeType,
    val bounds: AttributeBounds,
    var percentRoll: Double
) : CustomAttribute(type, RollType.SINGLE) {
    fun getAbsolutRoll() =
        bounds.min + (bounds.max - bounds.min) * percentRoll
}