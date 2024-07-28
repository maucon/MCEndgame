package de.fuballer.mcendgame.component.item.attribute.data

import kotlin.random.Random

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

    override fun roll(percentRoll: Double) = DoubleRoll(this, percentRoll)
}

data class StringBounds(
    val options: List<String>
) : AttributeBounds<StringRoll> {
    constructor(vararg options: String) : this(options.toList())

    override fun roll(percentRoll: Double) = StringRoll(this, Random.nextInt(options.size))
}

data class IntBounds(
    val min: Int,
    val max: Int
) : AttributeBounds<IntRoll> {
    init {
        assert(min <= max) { "min must be smaller than max" }
    }

    constructor(max: Int) : this(0, max)

    override fun roll(percentRoll: Double) = IntRoll(this, percentRoll)
}