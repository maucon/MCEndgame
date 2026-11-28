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

class DirectionalFlameSweepParticleEffect(
    val size: Double,
    val xDir: Double,
    val yDir: Double,
    val zDir: Double,
) : ParticleOptions {
    companion object {
        val CODEC: MapCodec<DirectionalFlameSweepParticleEffect> = RecordCodecBuilder.mapCodec(
            Function { instance ->
                instance.group(
                    Codec.DOUBLE.fieldOf("size").forGetter(Function { particleEffect -> particleEffect.size }),
                    Codec.DOUBLE.fieldOf("xDir").forGetter(Function { particleEffect -> particleEffect.xDir }),
                    Codec.DOUBLE.fieldOf("yDir").forGetter(Function { particleEffect -> particleEffect.yDir }),
                    Codec.DOUBLE.fieldOf("zDir").forGetter(Function { particleEffect -> particleEffect.zDir }),
                ).apply(instance, ::DirectionalFlameSweepParticleEffect)
            }
        )

        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, DirectionalFlameSweepParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.DOUBLE, { it.size },
                ByteBufCodecs.DOUBLE, { it.xDir },
                ByteBufCodecs.DOUBLE, { it.yDir },
                ByteBufCodecs.DOUBLE, { it.zDir },
                ::DirectionalFlameSweepParticleEffect
            )
    }

    override fun getType(): ParticleType<*> = CustomParticleTypes.DIRECTIONAL_SWEEP_FLAME
}