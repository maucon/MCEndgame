package de.fuballer.mcendgame.main.component.custom_attribute.data

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asStringBounds
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asStringRoll

private const val PERCENT_FACTOR = 100.0

/**
 * Formats for a single attribute value, to be combined via [AttributeFormat.of].
 * Signed variants only change how the rolled value is shown; bounds are never signed.
 */
object ValueFormats {
    val INT = ValueFormat(
        formatRoll = { roll -> roll.getValue().toString() },
        formatBounds = { bounds -> formatIntBounds(bounds) },
    )
    val SIGNED_INT = ValueFormat(
        formatRoll = { roll -> "%+d".format(roll.asIntRoll().getValue()) },
        formatBounds = { bounds -> formatIntBounds(bounds) },
    )

    val DOUBLE = doubleFormat(scale = 1.0, signed = false)
    val SIGNED_DOUBLE = doubleFormat(scale = 1.0, signed = true)

    /** The value is stored as a fraction (0.25) and shown as a percentage (25). */
    val PERCENT = doubleFormat(scale = PERCENT_FACTOR, signed = false)
    val SIGNED_PERCENT = doubleFormat(scale = PERCENT_FACTOR, signed = true)

    val STRING = ValueFormat(
        formatRoll = { roll -> roll.asStringRoll().getValue() },
        formatBounds = { bounds -> bounds.asStringBounds().options.joinToString(prefix = "(", postfix = ")") },
    )

    /** Formats with one decimal, but drops the decimal if it is zero (2.5 -> "2.5", 3.0 -> "3"). */
    fun formatDouble(value: Double) = formatWithoutZeroDecimal("%.1f", "%.0f", value)

    fun formatDoubleSigned(value: Double) = formatWithoutZeroDecimal("%+.1f", "%+.0f", value)

    private fun formatWithoutZeroDecimal(withDecimal: String, withoutDecimal: String, value: Double): String {
        val formatted = withDecimal.format(value)
        return if (formatted.endsWith("0")) withoutDecimal.format(value) else formatted
    }

    private fun doubleFormat(scale: Double, signed: Boolean) = ValueFormat(
        formatRoll = { roll ->
            val value = roll.asDoubleRoll().getValue() * scale
            if (signed) formatDoubleSigned(value) else formatDouble(value)
        },
        formatBounds = { bounds ->
            val range = bounds.asDoubleBounds()
            formatRange(formatDouble(range.min * scale), formatDouble(range.max * scale))
        },
    )

    private fun formatIntBounds(bounds: AttributeBounds<*>): String {
        val range = bounds.asIntBounds()
        return formatRange(range.min.toString(), range.max.toString())
    }

    /** "(5)" if both ends are equal, otherwise "(5-10)". */
    private fun formatRange(min: String, max: String) = if (min == max) "($min)" else "($min-$max)"
}