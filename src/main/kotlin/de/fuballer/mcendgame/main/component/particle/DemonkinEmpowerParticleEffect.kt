package de.fuballer.mcendgame.main.component.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.UUIDUtil
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import java.util.function.Function

class DemonkinEmpowerParticleEffect(
    val targetEntityId: UUID,
    val duration: Int,
) : ParticleOptions {
    companion object {
        val CODEC: MapCodec<DemonkinEmpowerParticleEffect> = RecordCodecBuilder.mapCodec(
            Function { instance ->
                instance.group(
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("target_entity_id").forGetter(Function { particleEffect -> particleEffect.targetEntityId }),
                    Codec.INT.fieldOf("duration").forGetter(Function { particleEffect -> particleEffect.duration }),
                ).apply(instance, ::DemonkinEmpowerParticleEffect)
            }
        )

        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, DemonkinEmpowerParticleEffect> =
            StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, { it.targetEntityId },
                ByteBufCodecs.INT, { it.duration },
                ::DemonkinEmpowerParticleEffect
            )
    }

    override fun getType(): ParticleType<*> = CustomParticleTypes.DEMONKIN_EMPOWER
}