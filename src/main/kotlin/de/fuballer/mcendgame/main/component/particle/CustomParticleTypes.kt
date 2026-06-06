package de.fuballer.mcendgame.main.component.particle

import com.mojang.serialization.MapCodec
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.function.Function

@Injectable
object CustomParticleTypes {
    val FLAME_PILLAR = registerSimple("flame_pillar")
    val SMOKE_PILLAR = registerSimple("smoke_pillar")
    val HORIZONTAL_FLAME_BREATH = registerComplex<HorizontalFlameBreathParticleEffect>(
        "horizontal_flame_breath",
        false,
        { _ -> HorizontalFlameBreathParticleEffect.CODEC },
        { _ -> HorizontalFlameBreathParticleEffect.PACKET_CODEC },
    )
    val MOVE_TO_TARGET_FLAME = registerComplex<MoveToTargetFlameParticleEffect>(
        "move_to_target_flame",
        false,
        { _ -> MoveToTargetFlameParticleEffect.CODEC },
        { _ -> MoveToTargetFlameParticleEffect.PACKET_CODEC },
    )

    private fun registerSimple(name: String): SimpleParticleType =
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, IdentifierUtil.default(name), FabricParticleTypes.simple())

    private fun <T : ParticleOptions> registerComplex(
        name: String,
        alwaysShow: Boolean,
        codecGetter: Function<ParticleType<T>, MapCodec<T>>,
        packetCodecGetter: Function<ParticleType<T>, StreamCodec<in RegistryFriendlyByteBuf, T>>
    ): ParticleType<T> {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, IdentifierUtil.default(name), object : ParticleType<T>(alwaysShow) {
            override fun codec(): MapCodec<T> {
                return codecGetter.apply(this)
            }

            override fun streamCodec(): StreamCodec<in RegistryFriendlyByteBuf, T> {
                return packetCodecGetter.apply(this)
            }
        })
    }
}