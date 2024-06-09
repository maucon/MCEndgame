package de.fuballer.mcendgame.component.item.attribute

data class RollableAttribute(
    val type: AttributeType,
    val rollType: RollType,
    /**Should be present if [rollType] is not equal to [RollType.STATIC]*/
    val bounds: AttributeBounds? = null
) {
    constructor(type: AttributeType)
            : this(type, RollType.STATIC)

    constructor(type: AttributeType, min: Double, max: Double)
            : this(type, RollType.SINGLE, AttributeBounds(min, max))

    constructor(type: AttributeType, max: Double)
            : this(type, RollType.SINGLE, AttributeBounds(0.0, max))

    fun roll(percentRoll: Double): CustomAttribute {
        val bounds = bounds ?: throw IllegalStateException("bounds not present")
        val value = bounds.min + (bounds.max - bounds.min) * percentRoll

        return SingleValueAttribute(type, bounds, value)
    }
}

data class AttributeBounds(
    val min: Double,
    val max: Double
) {
    init {
        if (min > max) throw IllegalArgumentException("min cannot be greater than max")
    }
}

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

data class BaseAttribute(
    val type: AttributeType,
    var roll: Double
)