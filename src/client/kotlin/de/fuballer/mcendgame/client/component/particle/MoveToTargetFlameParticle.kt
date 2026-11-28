package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.MoveToTargetFlameParticleEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.RandomSource
import java.util.*

class MoveToTargetFlameParticle(
    clientWorld: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    targetId: UUID,
    duration: Int,
    sprite: TextureAtlasSprite,
) : MoveToTargetParticle(clientWorld, x, y, z, targetId, duration, sprite) {
    class Factory(
        private val spriteProvider: SpriteSet,
    ) : ParticleProvider<MoveToTargetFlameParticleEffect> {
        override fun createParticle(
            particleEffect: MoveToTargetFlameParticleEffect,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: RandomSource,
        ): Particle {
            return MoveToTargetFlameParticle(
                clientWorld,
                x,
                y,
                z,
                particleEffect.targetEntityId,
                particleEffect.duration,
                spriteProvider.get(random)
            )
        }
    }
}