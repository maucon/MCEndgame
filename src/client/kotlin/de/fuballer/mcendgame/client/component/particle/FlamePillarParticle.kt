package de.fuballer.mcendgame.client.component.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.RisingParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class FlamePillarParticle(
    clientWorld: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    sprite: TextureAtlasSprite,
) : RisingParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, sprite) {
    private var centerX = x
    private var centerZ = z
    private var angle = 6.28319 * Random.nextDouble()
    private var radius = 0.25 + 0.15 * Random.nextDouble()
    private var yVelocity = 0.2

    init {
        lifetime = 50
    }

    override fun tick() {
        super.tick()

        angle += 0.3
        radius += 0.015
        yVelocity -= 0.003

        val offsetX = cos(angle) * radius
        val offsetZ = sin(angle) * radius

        x = centerX + offsetX
        z = centerZ + offsetZ
        yd = yVelocity
    }

    override fun getQuadSize(tickProgress: Float): Float {
        val lifespanPercent = (age + tickProgress) / lifetime
        return quadSize * (1.0f - lifespanPercent * lifespanPercent * 0.5f)
    }

    override fun getLayer(): Layer = Layer.OPAQUE

    class Factory(
        private val spriteProvider: SpriteSet,
    ) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            simpleParticleType: SimpleParticleType,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: RandomSource,
        ): Particle {
            return FlamePillarParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.get(random))
        }
    }
}