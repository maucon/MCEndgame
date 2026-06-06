package de.fuballer.mcendgame.main.component.custom_attribute.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.Affinity
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinity
import de.fuballer.mcendgame.main.util.minecraft.CodecUtil
import net.minecraft.ChatFormatting
import kotlin.math.roundToInt
import kotlin.random.Random

sealed interface AttributeRoll<T> {
    companion object {
        val CODEC: Codec<AttributeRoll<*>> = CodecUtil.ofThree(DoubleRoll.CODEC, StringRoll.CODEC, IntRoll.CODEC)
        const val DEFAULT_AFFINITY_BASED_ROLL_PERCENTAGE = 0.5
    }

    val bounds: AttributeBounds<*>
    val enhancements: Map<EnhancementType, Double>

    fun getValue(): T

    fun isNegative(): Boolean

    fun isPositive() = !isNegative()

    fun getSignFlipped(): AttributeRoll<T>

    fun hasNonZeroRange(): Boolean

    fun getRerolled(): AttributeRoll<T>

    fun getAffinityBasedRollPercentage(
        affinityConfig: List<AttributeAffinity>,
        rolls: List<AttributeRoll<*>>,
        index: Int,
    ): Double

    fun getEnhanced(
        value: Double,
        enhancementType: EnhancementType,
        affinity: Affinity,
    ): AttributeRoll<T>

    fun getAffinityBasedEnhanceValue(
        value: Double,
        affinity: Affinity,
    ) = when (affinity) {
        Affinity.BENEFICIAL -> value
        Affinity.DETRIMENTAL -> -value
        Affinity.NEUTRAL -> if (isPositive()) value else -value
    }

    fun getPercentRollOrNull(): Double?

    fun hasPercentRoll() = getPercentRollOrNull() != null

    fun getWithPercentRoll(percent: Double): AttributeRoll<T>

    fun canBeEnhanced(): Boolean

    fun getTotalEnhanceFactor() = 1 + enhancements.values.sum()

    fun getWithoutEnhancement(): AttributeRoll<T>

    fun getDominantEnhancementColor() = enhancements.keys.minByOrNull { it.ordinal }?.color

    enum class EnhancementType(
        val color: ChatFormatting,
    ) {
        CORRUPTION(ChatFormatting.DARK_RED),
        SACRIFICE(ChatFormatting.RED);

        companion object {
            val CODEC: Codec<EnhancementType> =
                Codec.STRING.xmap(
                    EnhancementType::valueOf,
                    EnhancementType::name
                )
        }
    }
}

data class DoubleRoll(
    override val bounds: DoubleBounds,
    var percentRoll: Double = 1.0,
    override val enhancements: Map<AttributeRoll.EnhancementType, Double> = mapOf(),
    val format: String = "%.0f",
) : AttributeRoll<Double> {
    companion object {
        val CODEC: Codec<DoubleRoll> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    DoubleBounds.CODEC.fieldOf("bounds").forGetter(DoubleRoll::bounds),
                    Codec.DOUBLE.fieldOf("percentRoll").forGetter(DoubleRoll::percentRoll),
                    Codec.unboundedMap(AttributeRoll.EnhancementType.CODEC, Codec.DOUBLE)
                        .optionalFieldOf("enhancements", mapOf()).forGetter(DoubleRoll::enhancements),
                ).apply(instance, ::DoubleRoll)
            }
    }

    override fun getValue() = (bounds.min + (bounds.max - bounds.min) * percentRoll) * getTotalEnhanceFactor()

    override fun isNegative() = getValue() < 0

    override fun getSignFlipped() = DoubleRoll(bounds.getSignFlipped(), 1 - percentRoll, enhancements, format)

    override fun hasNonZeroRange() = bounds.min < bounds.max

    override fun getRerolled() = DoubleRoll(bounds, Random.nextDouble(), enhancements, format)

    override fun getAffinityBasedRollPercentage(
        affinityConfig: List<AttributeAffinity>,
        rolls: List<AttributeRoll<*>>,
        index: Int,
    ): Double {
        val affinity = affinityConfig[index].getAffinity(affinityConfig, rolls, index)
        if (affinity == Affinity.NEUTRAL) return if (isPositive()) percentRoll else 1 - percentRoll
        if (bounds.min == bounds.max) return AttributeRoll.DEFAULT_AFFINITY_BASED_ROLL_PERCENTAGE

        return if (affinity == Affinity.BENEFICIAL) percentRoll else 1 - percentRoll
    }

    override fun canBeEnhanced() = true

    override fun getEnhanced(
        value: Double,
        enhancementType: AttributeRoll.EnhancementType,
        affinity: Affinity,
    ): DoubleRoll {
        val affinityBasedValue = getAffinityBasedEnhanceValue(value, affinity)
        val newEnhancements = enhancements + mapOf(enhancementType to (enhancements[enhancementType] ?: 0.0) + affinityBasedValue)
        return DoubleRoll(bounds, percentRoll, newEnhancements, format)
    }

    override fun getPercentRollOrNull() = if (hasNonZeroRange()) percentRoll else null

    override fun getWithPercentRoll(percent: Double) = DoubleRoll(bounds, percent, enhancements, format)

    override fun getWithoutEnhancement() = DoubleRoll(bounds, percentRoll, format = format)
}

data class StringRoll(
    override val bounds: StringBounds,
    var indexRoll: Int = 0,
    override val enhancements: Map<AttributeRoll.EnhancementType, Double> = mapOf(),
) : AttributeRoll<String> {
    companion object {
        val CODEC: Codec<StringRoll> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    StringBounds.CODEC.fieldOf("bounds").forGetter(StringRoll::bounds),
                    Codec.INT.fieldOf("indexRoll").forGetter(StringRoll::indexRoll),
                    Codec.unboundedMap(AttributeRoll.EnhancementType.CODEC, Codec.DOUBLE)
                        .optionalFieldOf("enhancementsS", mapOf()).forGetter(StringRoll::enhancements),
                ).apply(instance, ::StringRoll)
            }
    }

    override fun getValue() = bounds.options[indexRoll]

    override fun isNegative() = false

    override fun getSignFlipped() = StringRoll(bounds.getSignFlipped(), indexRoll)

    override fun hasNonZeroRange() = bounds.options.size > 1

    override fun getRerolled() = StringRoll(bounds, Random.nextInt(bounds.options.size))

    override fun getAffinityBasedRollPercentage(
        affinityConfig: List<AttributeAffinity>,
        rolls: List<AttributeRoll<*>>,
        index: Int,
    ) = AttributeRoll.DEFAULT_AFFINITY_BASED_ROLL_PERCENTAGE

    override fun canBeEnhanced() = false

    override fun getEnhanced(
        value: Double,
        enhancementType: AttributeRoll.EnhancementType,
        affinity: Affinity,
    ) = copy()

    override fun getPercentRollOrNull() = null

    override fun getWithPercentRoll(percent: Double) = StringRoll(bounds, indexRoll)

    override fun getTotalEnhanceFactor() = 1.0

    override fun getWithoutEnhancement() = StringRoll(bounds, indexRoll)
}

data class IntRoll(
    override val bounds: IntBounds,
    var percentRoll: Double = 1.0,
    override val enhancements: Map<AttributeRoll.EnhancementType, Double> = mapOf(),
) : AttributeRoll<Int> {
    companion object {
        val CODEC: Codec<IntRoll> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    IntBounds.CODEC.fieldOf("bounds").forGetter(IntRoll::bounds),
                    Codec.DOUBLE.fieldOf("percentRollI").forGetter(IntRoll::percentRoll), // percentRollI to differentiate between DoubleRoll
                    Codec.unboundedMap(AttributeRoll.EnhancementType.CODEC, Codec.DOUBLE)
                        .optionalFieldOf("enhancementsI", mapOf()).forGetter(IntRoll::enhancements),
                ).apply(instance, ::IntRoll)
            }
    }

    override fun getValue() = ((bounds.min + (bounds.max - bounds.min) * percentRoll) * getTotalEnhanceFactor()).roundToInt()

    override fun isNegative() = getValue() < 0

    override fun getSignFlipped() = IntRoll(bounds.getSignFlipped(), 1 - percentRoll, enhancements)

    override fun hasNonZeroRange() = bounds.min < bounds.max

    override fun getRerolled() = IntRoll(bounds, Random.nextDouble(), enhancements)

    override fun getAffinityBasedRollPercentage(
        affinityConfig: List<AttributeAffinity>,
        rolls: List<AttributeRoll<*>>,
        index: Int,
    ): Double {
        val affinity = affinityConfig[index].getAffinity(affinityConfig, rolls, index)
        if (affinity == Affinity.NEUTRAL) return if (isPositive()) percentRoll else 1 - percentRoll
        if (bounds.min == bounds.max) return AttributeRoll.DEFAULT_AFFINITY_BASED_ROLL_PERCENTAGE

        return if (affinity == Affinity.BENEFICIAL) percentRoll else 1 - percentRoll
    }

    override fun canBeEnhanced() = true

    override fun getEnhanced(
        value: Double,
        enhancementType: AttributeRoll.EnhancementType,
        affinity: Affinity,
    ): IntRoll {
        val affinityBasedValue = getAffinityBasedEnhanceValue(value, affinity)
        val newEnhancements = enhancements + mapOf(enhancementType to (enhancements[enhancementType] ?: 0.0) + affinityBasedValue)
        return IntRoll(bounds, percentRoll, newEnhancements)
    }

    override fun getPercentRollOrNull() = if (hasNonZeroRange()) percentRoll else null

    override fun getWithPercentRoll(percent: Double) = IntRoll(bounds, percent, enhancements)

    override fun getWithoutEnhancement() = IntRoll(bounds, percentRoll)
}