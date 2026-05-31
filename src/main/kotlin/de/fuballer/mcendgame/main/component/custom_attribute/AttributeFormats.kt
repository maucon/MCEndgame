package de.fuballer.mcendgame.main.component.custom_attribute

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asStringBounds
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asStringRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeRoll

object AttributeFormats {
    val EMPTY_ROLL = { _: List<AttributeRoll<*>> -> listOf<String>() }
    val PERCENT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDouble(rolls[0].asDoubleRoll().getValue() * 100))
    }
    val SIGNED_PERCENT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDoubleSigned(rolls[0].asDoubleRoll().getValue() * 100))
    }
    val STRING_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(rolls[0].asStringRoll().getValue())
    }
    val TWO_STRING_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(rolls[0].asStringRoll().getValue(), rolls[1].asStringRoll().getValue())
    }
    val INT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(rolls[0].getValue().toString())
    }
    val SIGNED_INT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(String.format("%+d", rolls[0].asIntRoll().getValue()))
    }
    val TWO_INT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(rolls[0].getValue().toString(), rolls[1].getValue().toString())
    }
    val DOUBLE_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDouble(rolls[0].asDoubleRoll().getValue()))
    }
    val SIGNED_DOUBLE_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDoubleSigned(rolls[0].asDoubleRoll().getValue()))
    }
    val PERCENT_AND_INT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDouble(rolls[0].asDoubleRoll().getValue() * 100), rolls[1].getValue().toString())
    }
    val INT_AND_PERCENT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(rolls[0].getValue().toString(), formatDouble(rolls[1].asDoubleRoll().getValue() * 100))
    }
    val DOUBLE_AND_INT_AND_PERCENT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(
            formatDouble(rolls[0].asDoubleRoll().getValue()),
            rolls[1].getValue().toString(),
            formatDouble(rolls[2].asDoubleRoll().getValue() * 100),
        )
    }
    val INT_AND_DOUBLE_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(rolls[0].getValue().toString(), formatDouble(rolls[1].asDoubleRoll().getValue()))
    }
    val SIGNED_PERCENT_AND_INT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDoubleSigned(rolls[0].asDoubleRoll().getValue() * 100), rolls[1].getValue().toString())
    }
    val TWO_PERCENT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDouble(rolls[0].asDoubleRoll().getValue() * 100), formatDouble(rolls[1].asDoubleRoll().getValue() * 100))
    }
    val SIGNED_PERCENT_AND_PERCENT_ROLL = { rolls: List<AttributeRoll<*>> ->
        listOf(formatDoubleSigned(rolls[0].asDoubleRoll().getValue() * 100), formatDouble(rolls[1].asDoubleRoll().getValue() * 100))
    }

    val EMPTY_BOUNDS = { _: List<AttributeBounds<*>> -> listOf<String>() }
    val PERCENT_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound = bounds[0].asDoubleBounds()
        listOf(getRangeOrValue(formatDouble(bound.min * 100), formatDouble(bound.max * 100)))
    }
    val TWO_PERCENT_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound1 = bounds[0].asDoubleBounds()
        val bound2 = bounds[1].asDoubleBounds()
        listOf(
            getRangeOrValue(formatDouble(bound1.min * 100), formatDouble(bound1.max * 100)),
            getRangeOrValue(formatDouble(bound2.min * 100), formatDouble(bound2.max * 100)),
        )
    }
    val STRING_SHOW_ALL_OPTIONS = { bounds: List<AttributeBounds<*>> ->
        listOf(bounds[0].asStringBounds().options.joinToString(prefix = "(", postfix = ")"))
    }
    val TWO_STRING_SHOW_ALL_OPTIONS = { bounds: List<AttributeBounds<*>> ->
        listOf(
            bounds[0].asStringBounds().options.joinToString(prefix = "(", postfix = ")"),
            bounds[1].asStringBounds().options.joinToString(prefix = "(", postfix = ")"),
        )
    }
    val INT_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound = bounds[0].asIntBounds()
        listOf(getRangeOrValue(bound.min.toString(), bound.max.toString()))
    }
    val TWO_INT_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound1 = bounds[0].asIntBounds()
        val bound2 = bounds[1].asIntBounds()
        listOf(
            getRangeOrValue(bound1.min.toString(), bound1.max.toString()),
            getRangeOrValue(bound2.min.toString(), bound2.max.toString()),
        )
    }
    val DOUBLE_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound = bounds[0].asDoubleBounds()
        listOf(getRangeOrValue(formatDouble(bound.min), formatDouble(bound.max)))
    }
    val PERCENT_AND_INT_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound1 = bounds[0].asDoubleBounds()
        val bound2 = bounds[1].asIntBounds()
        listOf(
            getRangeOrValue(formatDouble(bound1.min * 100), formatDouble(bound1.max * 100)),
            getRangeOrValue(bound2.min.toString(), bound2.max.toString()),
        )
    }
    val INT_AND_PERCENT_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound1 = bounds[0].asIntBounds()
        val bound2 = bounds[1].asDoubleBounds()
        listOf(
            getRangeOrValue(bound1.min.toString(), bound1.max.toString()),
            getRangeOrValue(formatDouble(bound2.min * 100), formatDouble(bound2.max * 100)),
        )
    }
    val DOUBLE_AND_INT_AND_PERCENT_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound1 = bounds[0].asDoubleBounds()
        val bound2 = bounds[1].asIntBounds()
        val bound3 = bounds[2].asDoubleBounds()
        listOf(
            getRangeOrValue(formatDouble(bound1.min), formatDouble(bound1.max)),
            getRangeOrValue(bound2.min.toString(), bound2.max.toString()),
            getRangeOrValue(formatDouble(bound3.min * 100), formatDouble(bound3.max * 100)),
        )
    }
    val INT_AND_DOUBLE_BOUNDS = { bounds: List<AttributeBounds<*>> ->
        val bound1 = bounds[0].asIntBounds()
        val bound2 = bounds[1].asDoubleBounds()
        listOf(
            getRangeOrValue(bound1.min.toString(), bound1.max.toString()),
            getRangeOrValue(formatDouble(bound2.min), formatDouble(bound2.max)),
        )
    }

    fun formatDouble(value: Double): String {
        val formatted = "%.1f".format(value)
        if (formatted.endsWith("0")) {
            return "%.0f".format(value)
        }
        return formatted
    }

    fun formatDoubleSigned(value: Double): String {
        val formatted = "%+.1f".format(value)
        if (formatted.endsWith("0")) {
            return "%+.0f".format(value)
        }
        return formatted
    }

    private fun getRangeOrValue(val1: String, val2: String) = if (val1 == val2) "($val1)" else "($val1-$val2)"
}