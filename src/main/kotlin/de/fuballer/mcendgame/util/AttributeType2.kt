package de.fuballer.mcendgame.util

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import kotlin.random.Random

abstract class AttributeType(
    val lore: (List<AttributeRoll<out Any>>) -> String
)

class VanillaAttributeType(
    lore: (List<AttributeRoll<out Any>>) -> String,
    val attribute: Attribute,
    val scaleType: AttributeModifier.Operation
) : AttributeType(lore)

class CustomAttributeType(
    lore: (List<AttributeRoll<out Any>>) -> String
) : AttributeType(lore)

// ##################################################################################################

interface AttributeRollBounds<T : AttributeRoll<out Any>> {
    fun roll(): T
}

data class DoubleRollBounds(
    val min: Double,
    val max: Double
) : AttributeRollBounds<DoubleRoll> {
    init {
        assert(min <= max) { "min must be smaller than or equal max" }
    }

    override fun roll() = DoubleRoll(this, Random.nextDouble())
}

data class StringRollBounds(
    val rollables: List<String>
) : AttributeRollBounds<StringRoll> {
    override fun roll() = StringRoll(this, Random.nextInt(rollables.size))
}

data class IntRollBounds(
    val min: Int,
    val max: Int
) : AttributeRollBounds<IntRoll> {
    init {
        assert(min <= max) { "min must be smaller than or equal max" }
    }

    override fun roll() = IntRoll(this, Random.nextDouble())
}

// ##################################################################################################

interface AttributeRoll<T> {
    fun getAbsoluteRoll(): T
}

data class DoubleRoll(
    val bounds: DoubleRollBounds,
    val percentRoll: Double,
) : AttributeRoll<Double> {
    override fun getAbsoluteRoll() = bounds.min + (bounds.max - bounds.min) * percentRoll
}

data class StringRoll(
    val bounds: StringRollBounds,
    val indexRoll: Int,
) : AttributeRoll<String> {
    override fun getAbsoluteRoll() = bounds.rollables[indexRoll]
}

data class IntRoll(
    val bounds: IntRollBounds,
    val percentRoll: Double,
) : AttributeRoll<Int> {
    override fun getAbsoluteRoll() = (bounds.min + (bounds.max - bounds.min) * percentRoll).toInt()
}

// ##################################################################################################

data class RollableAttribute(
    val type: AttributeType,
    val bounds: List<AttributeRollBounds<*>>
) {
    fun roll() = RolledAttribute(type, bounds.map { it.roll() })
}

data class RolledAttribute(
    val type: AttributeType,
    val attributeRolls: List<AttributeRoll<out Any>>
) {
    fun getLore() = type.lore(attributeRolls)
}

// ##################################################################################################

object AttributeTypes {
    val ATTACK_DAMAGE = VanillaAttributeType({ "+${it[0].getAbsoluteRoll()}% Attack Damage" }, Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER)
    val DISABLE_MELEE = CustomAttributeType { "No Melee Damage frfr" }
    val EFFECT_IMMUNITY = CustomAttributeType { " ${it[0].getAbsoluteRoll()}% Chance to avoid Effect ${it[1].getAbsoluteRoll()}" }
}

object Attributes {
    val SWORD_ATTACK_DAMAGE = RollableAttribute(AttributeTypes.ATTACK_DAMAGE, listOf(DoubleRollBounds(0.0, 3.0)))
    val DISABLE_MELEE = RollableAttribute(AttributeTypes.DISABLE_MELEE, listOf())
    val EFFECT_IMMUNITY = RollableAttribute(AttributeTypes.EFFECT_IMMUNITY, listOf(DoubleRollBounds(0.3, 0.7), StringRollBounds(listOf("WITHER", "SLOWNESS", "WEAKNESS"))))
}

fun main() {
    val r1 = Attributes.SWORD_ATTACK_DAMAGE.roll()
    val r2 = Attributes.EFFECT_IMMUNITY.roll()
    val r3 = Attributes.DISABLE_MELEE.roll()

    println(r1)
    println(r1.getLore())
    println(r2)
    println(r2.getLore())
    println(r3)
    println(r3.getLore())
}