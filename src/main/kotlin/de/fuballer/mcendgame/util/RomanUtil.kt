package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll

object RomanUtil {
    private val intToRomanMap = mapOf(
        1000 to "M",
        900 to "CM",
        500 to "D",
        400 to "CD",
        100 to "C",
        90 to "XC",
        50 to "L",
        40 to "XL",
        10 to "X",
        9 to "IX",
        5 to "V",
        4 to "IV",
        1 to "I",
    )

    fun getAsRoman(attribute: AttributeRoll<*>): String {
        return getAsRoman(attribute.getRoll() as Int)
    }

    private fun getAsRoman(number: Int): String {
        val roman = StringBuilder()
        var remaining = number

        for (key in intToRomanMap.keys) {
            val repeats = remaining / key
            remaining -= repeats * key
            roman.append(intToRomanMap[key]!!.repeat(repeats))
        }

        return roman.toString()
    }
}