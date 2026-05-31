package de.fuballer.mcendgame.main.component.particle

import com.mojang.serialization.MapCodec
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
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
        Registry.register(Registries.PARTICLE_TYPE, IdentifierUtil.default(name), FabricParticleTypes.simple())

    private fun <T : ParticleEffect> registerComplex(
        name: String,
        alwaysShow: Boolean,
        codecGetter: Function<ParticleType<T>, MapCodec<T>>,
        packetCodecGetter: Function<ParticleType<T>, PacketCodec<in RegistryByteBuf, T>>
    ): ParticleType<T> {
        return Registry.register(Registries.PARTICLE_TYPE, IdentifierUtil.default(name), object : ParticleType<T>(alwaysShow) {
            override fun getCodec(): MapCodec<T> {
                return codecGetter.apply(this)
            }

            override fun getPacketCodec(): PacketCodec<in RegistryByteBuf, T> {
                return packetCodecGetter.apply(this)
            }
        })
    }
}