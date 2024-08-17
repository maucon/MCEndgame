package de.fuballer.mcendgame.component.item.attribute.data

import kotlin.math.roundToInt

sealed interface AttributeRoll<T> {
    val bounds: AttributeBounds<*>

    fun getRoll(): T
}

data class DoubleRoll(
    override val bounds: DoubleBounds,
    var percentRoll: Double,
) : AttributeRoll<Double> {
    override fun getRoll() = bounds.min + (bounds.max - bounds.min) * percentRoll
    override fun toString(): String = getRoll().toString()
}

data class StringRoll(
    override val bounds: StringBounds,
    var indexRoll: Int,
) : AttributeRoll<String> {
    override fun getRoll() = bounds.options[indexRoll]
    override fun toString(): String = getRoll()
}

data class IntRoll(
    override val bounds: IntBounds,
    var percentRoll: Double,
) : AttributeRoll<Int> {
    override fun getRoll() = (bounds.min + (bounds.max - bounds.min) * percentRoll).roundToInt()
    override fun toString(): String = getRoll().toString()
}