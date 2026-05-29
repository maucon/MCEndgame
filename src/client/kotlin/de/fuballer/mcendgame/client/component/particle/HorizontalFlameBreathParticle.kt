package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.HorizontalFlameBreathParticleEffect
import net.minecraft.client.particle.BillboardParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.texture.Sprite
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

class HorizontalFlameBreathParticle(
    clientWorld: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    directionX: Double,
    directionY: Double,
    directionZ: Double,
    spreadAngle: Double,
    sprite: Sprite,
) : BillboardParticle(clientWorld, x, y, z, 0.0, 0.0, 0.0, sprite) {
    init {
        maxAge = 40
        collidesWithWorld = false

        val randomAngle = Math.toRadians(Random.nextDouble() * spreadAngle - spreadAngle / 2)
        val direction = Vec3d(directionX, directionY, directionZ).rotateY(randomAngle.toFloat()).normalize()
        val velocity = direction.multiply(0.25)
        velocityX = velocity.x
        velocityY = velocity.y - 0.1
        velocityZ = velocity.z
    }

    override fun tick() {
        lastX = x
        lastY = y
        lastZ = z
        if (age++ >= maxAge) {
            markDead()
            return
        }

        if (velocityY < 0.03) velocityY += 0.008
        move(velocityX, velocityY, velocityZ)
    }

    override fun getSize(tickProgress: Float): Float {
        val time = (age + tickProgress) / maxAge.toFloat()

        return if (time <= 0.8f) {
            val normalized = time / 0.8f
            scale * (0.3f + normalized * 1.2f)
        } else {
            val normalized = (time - 0.8f) / 0.2f
            scale * (1.5f - normalized * 1.0f)
        }
    }

    override fun getRenderType(): RenderType = RenderType.PARTICLE_ATLAS_OPAQUE

    class Factory(
        private val spriteProvider: SpriteProvider,
    ) : ParticleFactory<HorizontalFlameBreathParticleEffect> {
        override fun createParticle(
            particleEffect: HorizontalFlameBreathParticleEffect,
            clientWorld: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: net.minecraft.util.math.random.Random,
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
                spriteProvider.getSprite(random)
            )
        }
    }
}