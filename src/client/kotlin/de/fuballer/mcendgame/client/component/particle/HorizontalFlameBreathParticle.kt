package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.HorizontalFlameBreathParticleEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class HorizontalFlameBreathParticle(
    clientWorld: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    directionX: Double,
    directionY: Double,
    directionZ: Double,
    spreadAngle: Double,
    sprite: TextureAtlasSprite,
) : SingleQuadParticle(clientWorld, x, y, z, 0.0, 0.0, 0.0, sprite) {
    init {
        lifetime = 40
        hasPhysics = false

        val randomAngle = Math.toRadians(Random.nextDouble() * spreadAngle - spreadAngle / 2)
        val direction = Vec3(directionX, directionY, directionZ).yRot(randomAngle.toFloat()).normalize()
        val velocity = direction.scale(0.25)
        xd = velocity.x
        yd = velocity.y - 0.1
        zd = velocity.z
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            remove()
            return
        }

        if (yd < 0.03) yd += 0.008
        move(xd, yd, zd)
    }

    override fun getQuadSize(tickProgress: Float): Float {
        val time = (age + tickProgress) / lifetime.toFloat()

        return if (time <= 0.8f) {
            val normalized = time / 0.8f
            quadSize * (0.3f + normalized * 1.2f)
        } else {
            val normalized = (time - 0.8f) / 0.2f
            quadSize * (1.5f - normalized * 1.0f)
        }
    }

    override fun getLayer(): Layer = Layer.OPAQUE

    class Factory(
        private val spriteProvider: SpriteSet,
    ) : ParticleProvider<HorizontalFlameBreathParticleEffect> {
        override fun createParticle(
            particleEffect: HorizontalFlameBreathParticleEffect,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: RandomSource,
        ): Particle {
            return HorizontalFlameBreathParticle(
                clientWorld,
                x,
                y,
                z,
                particleEffect.directionX,
                particleEffect.directionY,
                particleEffect.directionZ,
                particleEffect.spreadAngle,
                spriteProvider.get(random)
            )
        }
    }
}