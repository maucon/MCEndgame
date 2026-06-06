package de.fuballer.mcendgame.main.component.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.function.Function

class HorizontalFlameBreathParticleEffect(
    val directionX: Double,
    val directionY: Double,
    val directionZ: Double,
    val spreadAngle: Double
) : ParticleOptions {
    companion object {
        val CODEC: MapCodec<HorizontalFlameBreathParticleEffect> = RecordCodecBuilder.mapCodec(
            Function { instance ->
                instance.group(
                    Codec.DOUBLE.fieldOf("direction_x").forGetter(Function { particleEffect -> particleEffect.directionX }),
                    Codec.DOUBLE.fieldOf("direction_y").forGetter(Function { particleEffect -> particleEffect.directionY }),
                    Codec.DOUBLE.fieldOf("direction_z").forGetter(Function { particleEffect -> particleEffect.directionZ }),
                    Codec.DOUBLE.fieldOf("spread_angle").forGetter(Function { particleEffect -> particleEffect.spreadAngle }),
                ).apply(instance, ::HorizontalFlameBreathParticleEffect)
            }
        )

        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, HorizontalFlameBreathParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.DOUBLE, { it.directionX },
                ByteBufCodecs.DOUBLE, { it.directionY },
                ByteBufCodecs.DOUBLE, { it.directionZ },
                ByteBufCodecs.DOUBLE, { it.spreadAngle },
                ::HorizontalFlameBreathParticleEffect
            )
    }

    override fun getType(): ParticleType<*> = CustomParticleTypes.HORIZONTAL_FLAME_BREATH
}