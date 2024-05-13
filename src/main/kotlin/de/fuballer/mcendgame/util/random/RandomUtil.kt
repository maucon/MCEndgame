package de.fuballer.mcendgame.util.random

import kotlin.math.max
import kotlin.random.Random

object RandomUtil {
    fun <T : RandomOption<*>> pick(
        options: List<T>,
        random: Random = Random
    ): T {
        val totalWeight = options.sumOf { it.weight }
        val randomInt = random.nextInt(totalWeight)

        return pickOption(options, randomInt)
    }

    fun <T : SortableRandomOption<*>> pick(
        options: List<T>,
        rolls: Int,
        random: Random = Random
    ): T {
        val totalWeight = options.sumOf { it.weight }

        var randomInt = 0
        repeat(rolls) {
            randomInt = max(randomInt, random.nextInt(totalWeight))
        }

        val sortedOptions = options.sortedBy { it.tier }.toList()
        return pickOption(sortedOptions, randomInt)
    }

    fun <T> shuffle(
        options: List<RandomOption<T>>,
        random: Random = Random
    ): List<T> {
        val leftOptions = options.toMutableList()
        val shuffled = mutableListOf<T>()

        while (leftOptions.isNotEmpty()) {
            val picked = pick(leftOptions, random)

            shuffled.add(picked.option)
            leftOptions.remove(picked)
        }

        return shuffled
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
