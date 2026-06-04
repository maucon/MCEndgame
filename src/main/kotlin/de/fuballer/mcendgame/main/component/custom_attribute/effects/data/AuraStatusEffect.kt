package de.fuballer.mcendgame.main.component.custom_attribute.effects.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance

data class AuraStatusEffect(
    val type: Holder<MobEffect>,
    val amplifier: Int = 0,
    val applyDuration: Int = 60,
    val range: Int = 10,
) {
    fun getInstance() = MobEffectInstance(type, applyDuration, amplifier, true, true)

    companion object {
        val CODEC: Codec<AuraStatusEffect> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<AuraStatusEffect> ->
            instance.group(
                MobEffect.CODEC.fieldOf("id").forGetter { auraEffect: AuraStatusEffect -> auraEffect.type },
                Codec.INT.optionalFieldOf("amplifier", 0).forGetter { it.amplifier },
                Codec.INT.optionalFieldOf("apply_duration", 60).forGetter { it.applyDuration },
                Codec.INT.optionalFieldOf("range", 10).forGetter { it.range }
            ).apply(instance, ::AuraStatusEffect)
        }
    }
}