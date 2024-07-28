package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import java.text.DecimalFormat

object DecimalFormatExtension {
    fun DecimalFormat.formatDouble(attributeRoll: AttributeRoll<*>, multiplier: Int = 1): String {
        val roll = attributeRoll.getRoll() as Double
        return format(roll * multiplier)
    }
}