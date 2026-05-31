package de.fuballer.mcendgame.main.component.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.Uuids
import java.util.*
import java.util.function.Function

class MoveToTargetFlameParticleEffect(
    val targetEntityId: UUID,
    val duration: Int,
) : ParticleEffect {
    companion object {
        val CODEC: MapCodec<MoveToTargetFlameParticleEffect> = RecordCodecBuilder.mapCodec(
            Function { instance ->
                instance.group(
                    Uuids.CODEC.fieldOf("target_entity_id").forGetter(Function { particleEffect -> particleEffect.targetEntityId }),
                    Codec.INT.fieldOf("duration").forGetter(Function { particleEffect -> particleEffect.duration }),
                ).apply(instance, ::MoveToTargetFlameParticleEffect)
            }
        )

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, MoveToTargetFlameParticleEffect> =
            PacketCodec.tuple(
                Uuids.PACKET_CODEC, { it.targetEntityId },
                PacketCodecs.INTEGER, { it.duration },
                ::MoveToTargetFlameParticleEffect
            )
    }

    override fun getType(): ParticleType<*> = CustomParticleTypes.MOVE_TO_TARGET_FLAME
}