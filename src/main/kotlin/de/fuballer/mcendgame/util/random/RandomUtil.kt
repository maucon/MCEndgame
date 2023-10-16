package de.fuballer.mcendgame.util.random

import java.util.*
import kotlin.math.max

object RandomUtil {
    private val random = Random()

    fun <T : RandomOption<*>> pick(
        options: List<T>
    ): T {
        val totalWeight = options.sumOf { it.weight }
        val randomInt = random.nextInt(totalWeight)

        return pickOption(options, randomInt)
    }

    fun <T : SortableRandomOption<*>> pick(
        options: List<T>,
        rolls: Int
    ): T {
        val totalWeight = options.sumOf { it.weight }

        var randomInt = 0
        repeat(rolls) {
            randomInt = max(randomInt, random.nextInt(totalWeight))
        }

        val sortedOptions = options.sortedBy { it.tier }.toList()
        return pickOption(sortedOptions, randomInt)
    }

    private fun <T : RandomOption<*>> pickOption(
        options: List<T>,
        randomInt: Int
    ): T {
        var cumulatedWeight = 0

        for (option in options) {
            cumulatedWeight += option.weight
            if (cumulatedWeight <= randomInt) continue

            return option
        }

        throw IllegalStateException("Could not complete random pick")
    }
}
