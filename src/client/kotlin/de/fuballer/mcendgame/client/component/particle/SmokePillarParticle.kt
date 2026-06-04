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

class SmokePillarParticle(
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
    private var radius = 0.2 + 0.1 * Random.nextDouble()
    private var yVelocity = 0.2

    init {
        lifetime = 50

        val color = random.nextFloat() * 0.3F
        rCol = color
        gCol = color
        bCol = color
    }

    override fun tick() {
        super.tick()

        angle += 0.2
        radius += 0.01
        yVelocity -= 0.003

        val offsetX = cos(angle) * radius
        val offsetZ = sin(angle) * radius

        x = centerX + offsetX
        z = centerZ + offsetZ
        yd = yVelocity
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
            return SmokePillarParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.get(random))
        }
    }
}