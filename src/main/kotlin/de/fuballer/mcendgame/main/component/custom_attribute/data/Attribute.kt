package de.fuballer.mcendgame.main.component.custom_attribute.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.Uuids
import java.util.*
import kotlin.random.Random

data class RollableCustomAttribute(
    val type: AttributeType,
    val tier: Int,
    val bounds: List<AttributeBounds<*>>,
) {
    constructor(
        type: AttributeType,
        tier: Int,
        vararg bounds: AttributeBounds<*>
    ) : this(type, tier, bounds.toList())

    fun roll(percentRolls: List<Double>, slot: AttributeModifierSlot): CustomAttribute {
        require(bounds.size == percentRolls.size) { "number of percentRolls must be equal to the number of bounds" }
        val attributeRolls = bounds.zip(percentRolls)
            .map { (attributeBounds, percentRoll) -> attributeBounds.roll(percentRoll) }

        return CustomAttribute(type, tier, attributeRolls, slot)
    }

    fun roll(slot: AttributeModifierSlot = AttributeModifierSlot.ANY): CustomAttribute {
        val percentageRolls = mutableListOf<Double>()
        repeat(bounds.size) {
            percentageRolls.add(Random.nextDouble())
        }
        return roll(percentageRolls, slot)
    }
}

data class CustomAttribute(
    val type: AttributeType,
    val tier: Int = 0,
    val rolls: List<AttributeRoll<*>> = listOf(),
    val slot: AttributeModifierSlot = AttributeModifierSlot.ANY,
    val id: UUID = UUID.randomUUID(),
) {
    constructor(
        type: AttributeType,
        tier: Int = 0,
        roll: AttributeRoll<*>,
        slot: AttributeModifierSlot = AttributeModifierSlot.ANY,
        id: UUID = UUID.randomUUID(),
    ) : this(type, tier, listOf(roll), slot, id)

    companion object {
        val CODEC: Codec<CustomAttribute> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    AttributeType.CODEC.fieldOf("type").forGetter(CustomAttribute::type),
                    Codec.INT.fieldOf("tier").forGetter(CustomAttribute::tier),
                    AttributeRoll.CODEC.listOf().fieldOf("rolls").forGetter(CustomAttribute::rolls),
                    AttributeModifierSlot.CODEC.fieldOf("slot").forGetter(CustomAttribute::slot),
                    Uuids.CODEC.optionalFieldOf("id").forGetter { Optional.of(it.id) },
                ).apply(instance) { type, tier, rolls, slot, idOpt ->
                    CustomAttribute(type, tier, rolls, slot, idOpt.orElseGet { UUID.randomUUID() })
                }
            }
        val LIST_CODEC = CODEC.listOf()

        val PACKET_CODEC: PacketCodec<ByteBuf, CustomAttribute> = PacketCodecs.codec(CODEC)
        val LIST_PACKET_CODEC: PacketCodec<ByteBuf, List<CustomAttribute>> = PACKET_CODEC.collect(PacketCodecs.toList())

        val LIST_TRACKED_DATA_HANDLER: TrackedDataHandler<List<CustomAttribute>> = TrackedDataHandler.create(LIST_PACKET_CODEC)
            .also { TrackedDataHandlerRegistry.register(it) }
    }

    fun getNonZeroRangeCount() = rolls.count { it.hasNonZeroRange() }

    fun hasNonZeroRange() = getNonZeroRangeCount() > 0

    fun enhanceableRollCount() = rolls.count(AttributeRoll<*>::canBeEnhanced)

    fun canBeEnhanced() = enhanceableRollCount() > 0

    fun getRerolled() = CustomAttribute(type, tier, rolls.map { it.getRerolled() }, slot, id)

    fun getAffinityBasedRollPercentage(): Double {
        if (rolls.isEmpty()) return AttributeRoll.DEFAULT_AFFINITY_BASED_ROLL_PERCENTAGE
        return rolls.withIndex().map { it.value.getAffinityBasedRollPercentage(type.affinities, rolls, it.index) }.average()
    }

    fun getSingleRollEnhanced(
        value: Double,
        enhancementType: AttributeRoll.EnhancementType,
    ): CustomAttribute {
        val chosenRollIndex = rolls.withIndex().filter { it.value.canBeEnhanced() }.random().index
        val beneficial = type.affinities[chosenRollIndex].getAffinity(type.affinities, rolls, chosenRollIndex)

        val newRolls = rolls.toMutableList()
        newRolls[chosenRollIndex] = rolls[chosenRollIndex].getEnhanced(value, enhancementType, beneficial)

        return CustomAttribute(type, tier, newRolls, slot, id)
    }

    fun getNonZeroRangePercentRollCount() = rolls.count { it.hasNonZeroRange() && it.hasPercentRoll() }

    fun getPercentRolls() = rolls.mapNotNull { it.getPercentRollOrNull() }

    fun getWithRollPercentages(
        percentages: List<Double>,
    ): CustomAttribute {
        var percentagesUsed = 0
        val newRolls = rolls.map {
            if (!it.hasNonZeroRange()) it
            else it.getWithPercentRoll(percentages[percentagesUsed++])
        }
        return CustomAttribute(type, tier, newRolls, slot, id)
    }
}