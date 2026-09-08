package de.fuballer.mcendgame.main.component.custom_attribute.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlin.math.roundToInt
import kotlin.random.Random

sealed interface AttributeBounds<T : AttributeRoll<*>> {
    fun roll(percentRoll: Double): T
    fun getSignFlipped(): AttributeBounds<T>
    fun withFactor(factor: Double): AttributeBounds<T>
}

data class DoubleBounds(
    val min: Double,
    val max: Double = min,
) : AttributeBounds<DoubleRoll> {
    companion object {
        val CODEC: Codec<DoubleBounds> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.DOUBLE.fieldOf("min").forGetter(DoubleBounds::min),
                    Codec.DOUBLE.fieldOf("max").forGetter(DoubleBounds::max)
                ).apply(instance, ::DoubleBounds)
            }
    }

    init {
        require(min <= max) { "min must be smaller than max" }
    }

    override fun roll(percentRoll: Double) = DoubleRoll(this, percentRoll)

    override fun getSignFlipped() = DoubleBounds(-max, -min)

    override fun withFactor(factor: Double) = DoubleBounds(min * factor, max * factor)
}

data class StringBounds(
    val options: List<String>
) : AttributeBounds<StringRoll> {
    companion object {
        val CODEC: Codec<StringBounds> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.listOf().fieldOf("options").forGetter(StringBounds::options)
                ).apply(instance, ::StringBounds)
            }
    }

    constructor(vararg options: String) : this(options.toList())

    override fun roll(percentRoll: Double) = StringRoll(this, Random.nextInt(options.size))

    override fun getSignFlipped() = StringBounds(options)

    override fun withFactor(factor: Double) = StringBounds(options)
}

data class IntBounds(
    val min: Int,
    val max: Int = min,
) : AttributeBounds<IntRoll> {
    companion object {
        val CODEC: Codec<IntBounds> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.fieldOf("min").forGetter(IntBounds::min),
                    Codec.INT.fieldOf("max").forGetter(IntBounds::max)
                ).apply(instance, ::IntBounds)
            }
    }

    init {
        require(min <= max) { "min must be smaller than max" }
    }

    override fun roll(percentRoll: Double) = IntRoll(this, percentRoll)

    override fun getSignFlipped() = IntBounds(-max, -min)

    override fun withFactor(factor: Double) = IntBounds((min * factor).roundToInt(), (max * factor).roundToInt())
}