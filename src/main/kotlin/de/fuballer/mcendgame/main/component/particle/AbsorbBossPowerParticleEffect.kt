package de.fuballer.mcendgame.main.component.particle

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.UUIDUtil
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.*
import java.util.function.Function

class AbsorbBossPowerParticleEffect(
    val targetEntityId: UUID,
) : ParticleOptions {
    companion object {
        val CODEC: MapCodec<AbsorbBossPowerParticleEffect> = RecordCodecBuilder.mapCodec(
            Function { instance ->
                instance.group(
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("target_entity_id").forGetter(Function { particleEffect -> particleEffect.targetEntityId }),
                ).apply(instance, ::AbsorbBossPowerParticleEffect)
            }
        )

        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, AbsorbBossPowerParticleEffect> =
            StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, { it.targetEntityId },
                ::AbsorbBossPowerParticleEffect
            )
    }

    override fun getType(): ParticleType<*> = CustomParticleTypes.ABSORB_BOSS_POWER
}