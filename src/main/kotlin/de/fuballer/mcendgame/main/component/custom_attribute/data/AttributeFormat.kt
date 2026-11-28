package de.fuballer.mcendgame.main.component.custom_attribute.data

/**
 * Describes how a single value of an attribute is displayed:
 * the rolled value itself and the bounds it was rolled within.
 */
class ValueFormat(
    val formatRoll: (AttributeRoll<*>) -> String,
    val formatBounds: (AttributeBounds<*>) -> String,
)

/**
 * Describes how all values of an attribute are displayed, with one [ValueFormat] per value.
 *
 * Example: an attribute with a percentage and an integer value uses
 * `AttributeFormat.of(ValueFormats.PERCENT, ValueFormats.INT)`.
 */
class AttributeFormat private constructor(private val valueFormats: List<ValueFormat>) {
    fun formatRolls(rolls: List<AttributeRoll<*>>): List<String> =
        valueFormats.zip(rolls) { format, roll -> format.formatRoll(roll) }

    fun formatBounds(bounds: List<AttributeBounds<*>>): List<String> =
        valueFormats.zip(bounds) { format, valueBounds -> format.formatBounds(valueBounds) }

    companion object {
        /** For attributes without any displayed values. */
        val NONE = AttributeFormat(emptyList())

        fun of(vararg valueFormats: ValueFormat) = AttributeFormat(valueFormats.toList())
    }
}