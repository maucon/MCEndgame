package de.fuballer.mcendgame.component.item.attribute.data

import kotlin.random.Random

data class RollableCustomAttribute(
    val type: AttributeType,
    val bounds: List<AttributeBounds<*>>
) {
    constructor(
        type: AttributeType,
        vararg bounds: AttributeBounds<*>
    ) : this(type, bounds.toList())

    fun roll(): CustomAttribute {
        val attributeRolls = bounds.map { it.roll(Random.nextDouble()) }
        return CustomAttribute(type, attributeRolls)
    }

    fun roll(percentRoll: Double): CustomAttribute {
        val attributeRolls = bounds.map { it.roll(percentRoll) }
        return CustomAttribute(type, attributeRolls)
    }

    fun roll(percentRolls: List<Double>): CustomAttribute {
        require(bounds.size == percentRolls.size) { "number of percentRolls must be equal to the number of bounds" }
        val attributeRolls = bounds.zip(percentRolls)
            .map { (attributeBounds, percentRoll) -> attributeBounds.roll(percentRoll) }

        return CustomAttribute(type, attributeRolls)
    }
}

data class CustomAttribute(
    val type: AttributeType,
    val attributeRolls: List<AttributeRoll<*>>
) {
    fun getLore() = type.lore(attributeRolls)
}

data class BaseAttribute(
    val type: VanillaAttributeType,
    var amount: Double
) {
    fun getLore(): String {
        val roll = DoubleRoll(DoubleBounds(1.0), amount)
        return type.lore(listOf(roll))
    }
}