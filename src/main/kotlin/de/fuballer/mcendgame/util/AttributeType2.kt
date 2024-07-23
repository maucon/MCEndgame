package de.fuballer.mcendgame.util

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import java.text.DecimalFormat
import kotlin.random.Random

abstract class AttributeType(
    val lore: (List<AttributeRoll<*>>) -> String
)

class VanillaAttributeType(
    lore: (List<AttributeRoll<*>>) -> String,
    val attribute: Attribute,
    val scaleType: AttributeModifier.Operation
) : AttributeType(lore)

class CustomAttributeType(
    lore: (List<AttributeRoll<*>>) -> String
) : AttributeType(lore)

// ##################################################################################################

interface AttributeBounds<T : AttributeRoll<*>> {
    fun roll(percentRoll: Double): T
}

data class DoubleBounds(
    val min: Double,
    val max: Double
) : AttributeBounds<DoubleRoll> {
    init {
        assert(min <= max) { "min must be smaller than max" }
    }

    constructor(max: Double) : this(0.0, max)

    override fun roll(percentRoll: Double) =
        DoubleRoll(this, percentRoll)
}

data class StringBounds(
    val options: List<String>
) : AttributeBounds<StringRoll> {
    constructor(vararg options: String) : this(options.toList())

    override fun roll(percentRoll: Double) =
        StringRoll(this, Random.nextInt(options.size))
}

data class IntBounds(
    val min: Int,
    val max: Int
) : AttributeBounds<IntRoll> {
    init {
        assert(min <= max) { "min must be smaller than max" }
    }

    constructor(max: Int) : this(0, max)

    override fun roll(percentRoll: Double) =
        IntRoll(this, percentRoll)
}

// ##################################################################################################

interface AttributeRoll<T> {
    fun getRoll(): T
}

data class DoubleRoll(
    val bounds: DoubleBounds,
    val percentRoll: Double,
) : AttributeRoll<Double> {
    override fun getRoll() = bounds.min + (bounds.max - bounds.min) * percentRoll
}

data class StringRoll(
    val bounds: StringBounds,
    val indexRoll: Int,
) : AttributeRoll<String> {
    override fun getRoll() = bounds.options[indexRoll]
}

data class IntRoll(
    val bounds: IntBounds,
    val percentRoll: Double,
) : AttributeRoll<Int> {
    override fun getRoll() = (bounds.min + (bounds.max - bounds.min) * percentRoll).toInt()
}

// ##################################################################################################

data class RollableAttribute(
    val type: AttributeType,
    val bounds: List<AttributeBounds<*>>
) {
    constructor(
        type: AttributeType,
        vararg bounds: AttributeBounds<*>
    ) : this(type, bounds.toList())

    fun roll(): RolledAttribute {
        val attributeRolls = bounds.map { it.roll(Random.nextDouble()) }
        return RolledAttribute(type, attributeRolls)
    }

    fun roll(percentRoll: Double): RolledAttribute {
        val attributeRolls = bounds.map { it.roll(percentRoll) }
        return RolledAttribute(type, attributeRolls)
    }

    fun roll(percentRolls: List<Double>): RolledAttribute {
        assert(bounds.size == percentRolls.size) { "number of percentRolls must be equal to the number of bounds" }
        val attributeRolls = bounds.zip(percentRolls)
            .map { (attributeBounds, percentRoll) -> attributeBounds.roll(percentRoll) }

        return RolledAttribute(type, attributeRolls)
    }
}

data class RolledAttribute(
    val type: AttributeType,
    val attributeRolls: List<AttributeRoll<*>>
) {
    fun getLore() = type.lore(attributeRolls)
}

// ##################################################################################################

val PRECISE = DecimalFormat("#.##")

object AttributeTypes {
    val ATTACK_DAMAGE = VanillaAttributeType({ "+${PRECISE.format(it[0].getRoll())}% Attack Damage" }, Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER)
    val DISABLE_MELEE = CustomAttributeType { "No Melee Damage frfr" }
    val EFFECT_IMMUNITY = CustomAttributeType { " ${PRECISE.format((it[0].getRoll() as Double) * 100)}% Chance to avoid Effect ${it[1].getRoll()}" }
    val DAMAGE_INT = CustomAttributeType { "+${PRECISE.format(it[0].getRoll())}% Damage" }
}

object Attributes {
    val SWORD_ATTACK_DAMAGE = RollableAttribute(AttributeTypes.ATTACK_DAMAGE, DoubleBounds(3.0))
    val DISABLE_MELEE = RollableAttribute(AttributeTypes.DISABLE_MELEE)
    val EFFECT_IMMUNITY = RollableAttribute(AttributeTypes.EFFECT_IMMUNITY, DoubleBounds(0.3, 0.7), StringBounds("WITHER", "SLOWNESS", "WEAKNESS"))
    val DAMAGE_INT = RollableAttribute(AttributeTypes.DAMAGE_INT, IntBounds(3, 10))
}

fun main() {
    val r1 = Attributes.SWORD_ATTACK_DAMAGE.roll()

    val percentRolls = List(Attributes.EFFECT_IMMUNITY.bounds.size) { Random.nextDouble() }
    val r2 = Attributes.EFFECT_IMMUNITY.roll(percentRolls)

    val r3 = Attributes.DISABLE_MELEE.roll()
    val r4 = Attributes.DAMAGE_INT.roll(listOf(1.0))

    println(r1)
    println(r1.getLore())
    println(r2)
    println(r2.getLore())
    println(r3)
    println(r3.getLore())
    println(r4)
    println(r4.getLore())
}