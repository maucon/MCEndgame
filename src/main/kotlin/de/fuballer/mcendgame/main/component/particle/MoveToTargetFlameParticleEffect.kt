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

class MoveToTargetFlameParticleEffect(
    val targetEntityId: UUID,
    val duration: Int,
) : ParticleOptions {
    companion object {
        val CODEC: MapCodec<MoveToTargetFlameParticleEffect> = RecordCodecBuilder.mapCodec(
            Function { instance ->
                instance.group(
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("target_entity_id").forGetter(Function { particleEffect -> particleEffect.targetEntityId }),
                    Codec.INT.fieldOf("duration").forGetter(Function { particleEffect -> particleEffect.duration }),
                ).apply(instance, ::MoveToTargetFlameParticleEffect)
            }
        )

        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, MoveToTargetFlameParticleEffect> =
            StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, { it.targetEntityId },
                ByteBufCodecs.INT, { it.duration },
                ::MoveToTargetFlameParticleEffect
            )
    }

    override fun getType(): ParticleType<*> = CustomParticleTypes.MOVE_TO_TARGET_FLAME
}