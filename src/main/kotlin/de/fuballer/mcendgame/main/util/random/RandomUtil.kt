package de.fuballer.mcendgame.main.util.random

import kotlin.math.max
import kotlin.random.Random

/**
 * Utility functions for performing weighted random selections.
 *
 * Supports:
 * - Picking a single weighted option
 * - Picking multiple unique options
 * - Picking with repeats (forced or unrestricted)
 * - Weighted rolling with "tiers" (multiple rolls selecting the best result)
 * - Level-restricted option selection
 */
object RandomUtil {
    /**
     * Picks a single weighted option from the list.
     *
     * @param options List of weighted options.
     * @param random Random generator.
     * @return The selected option.
     */
    fun <T : RandomOption<*>> pickOne(
        options: List<T>,
        random: Random = Random
    ): T {
        val totalWeight = options.sumOf { it.weight }
        val roll = random.nextInt(totalWeight)
        return pickByRoll(options, roll)
    }

    /**
     * Picks multiple unique weighted options (no repeats).
     *
     * Stops early if there are not enough options remaining.
     *
     * @param options List of weighted options.
     * @param random Random generator.
     * @param amount Maximum amount of options to pick.
     */
    fun <T> pickUnique(
        options: List<RandomOption<T>>,
        random: Random,
        amount: Int
    ): List<T> {
        val remaining = options.toMutableList()
        val picked = mutableListOf<T>()

        repeat(amount) {
            if (remaining.isEmpty()) return picked

            val chosen = pickOne(remaining, random)
            picked += chosen.option
            remaining -= chosen
        }

        return picked
    }

    /**
     * Picks multiple options without repetition unless necessary.
     *
     * If the requested amount exceeds the number of available options,
     * full sets of all options are added until only a remainder needs random picking.
     */
    fun <T> pickRepeatIfNeeded(
        options: List<RandomOption<T>>,
        random: Random,
        amount: Int
    ): List<T> {
        val fullCycles = amount / options.size
        val remainder = amount % options.size

        val result = MutableList(fullCycles * options.size) {
            options[it % options.size].option
        }

        result += pickUnique(options, random, remainder)
        return result
    }

    /**
     * Picks multiple weighted options, allowing repeats freely.
     *
     * @param options Weighted options list.
     * @param amount How many to pick.
     */
    fun <T> pickWithRepeats(
        options: List<RandomOption<T>>,
        amount: Int,
        random: Random = Random,
    ): List<T> =
        List(amount) { pickOne(options, random).option }

    /**
     * Performs a weighted selection with multiple rolls, keeping the best roll.
     *
     * Higher rolls increase the chance of selecting higher-tier items.
     *
     * @param options List of sortable weighted options.
     * @param rolls Number of rolls performed.
     */
    fun <T : SortableRandomOption<*>> pickBestOf(
        options: List<T>,
        rolls: Int,
        random: Random = Random
    ): T {
        val totalWeight = options.sumOf { it.weight }

        var bestRoll = 0
        repeat(rolls) {
            bestRoll = max(bestRoll, random.nextInt(totalWeight))
        }

        val sorted = options.sortedBy { it.tier }
        return pickByRoll(sorted, bestRoll)
    }

    /**
     * Returns all options in a randomized order (weighted).
     *
     * Equivalent to repeatedly picking a weighted option without replacement.
     */
    fun <T> shuffleWeighted(
        options: List<RandomOption<T>>,
        random: Random = Random
    ): List<T> {
        val remaining = options.toMutableList()
        val result = mutableListOf<T>()

        while (remaining.isNotEmpty()) {
            val chosen = pickOne(remaining, random)
            result += chosen.option
            remaining -= chosen
        }

        return result
    }

    /**
     * Picks a level-restricted option using weighted tiered rolling.
     *
     * @param options List of level-restricted weighted options.
     * @param rolls Number of rolls to perform.
     * @param level Player or context level.
     */
    fun <T> pickLevelRestricted(
        options: List<LevelRestrictedRandomOption<T>>,
        rolls: Int,
        level: Int,
        random: Random = Random,
    ): T {
        val unlocked = options.filterNot { it.isLocked(level) }
        return pickBestOf(unlocked, rolls, random).option
    }

    /**
     * Picks multiple level-locked options with repeats allowed.
     */
    fun <T> pickLevelRestrictedWithRepeats(
        options: List<LevelRestrictedRandomOption<T>>,
        rolls: Int,
        level: Int,
        amount: Int,
        random: Random = Random,
    ): List<T> {
        val unlocked = options.filterNot { it.isLocked(level) }
        return List(amount) { pickBestOf(unlocked, rolls, random).option }
    }

    /**
     * Converts a probability (or expected count) into an integer number of rolls.
     *
     * The integer part of [probability] is always granted. The fractional part is
     * treated as the chance to grant one additional roll.
     *
     * Example: `2.3` yields 2 rolls, plus 1 more with a 30% chance.
     *
     * @param probability Expected number of rolls; must be non-negative.
     * @param random Random generator.
     * @return The guaranteed rolls plus the bonus roll, if it succeeded.
     */
    fun rollCount(
        probability: Double,
        random: Random = Random,
    ): Int {
        return probability.toInt() + if (random.nextDouble() < probability % 1) 1 else 0
    }

    /**
     * Internal helper that selects an option given a roll value.
     *
     * @throws IllegalStateException if no option can be matched.
     */
    private fun <T : RandomOption<*>> pickByRoll(
        options: List<T>,
        roll: Int
    ): T {
        var cumulative = 0

        for (option in options) {
            cumulative += option.weight
            if (roll < cumulative) return option
        }

        throw IllegalStateException("Could not complete random pick")
    }
}