package de.fuballer.mcendgame.component.item.attribute

import kotlin.math.pow
import kotlin.random.Random

private const val EXPONENT_TIER_SCALING = .05
private const val EXPONENT_TIER_OFFSET = 10

data class RollableAttribute(
    val type: AttributeType,
    val min: Double,
    val max: Double
) {
    constructor(
        type: AttributeType,
        max: Double
    ) : this(type, 0.0, max)

    constructor(type: AttributeType) : this(type, 0.0)

    init {
        if (min > max) throw IllegalArgumentException("min cannot be greater than max")
    }

    fun roll(mapTier: Int): RolledAttribute {
        val roll = calculateRoll(mapTier)
        val value = min + (max - min) * roll

        return RolledAttribute(type, value)
    }

    fun roll(roll: Double): RolledAttribute {
        val value = min + (max - min) * roll
        return RolledAttribute(type, value)
    }

    private fun calculateRoll(mapTier: Int): Double {
        val random = Random.nextDouble()
        if (mapTier <= EXPONENT_TIER_OFFSET) return random

        return 1 - random.pow(1 + EXPONENT_TIER_SCALING * (mapTier - EXPONENT_TIER_OFFSET))
    }
}