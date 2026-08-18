package de.fuballer.mcendgame.client.component.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import kotlin.math.max

class CloudTorusParticle(
    clientWorld: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xDist: Double,
    yDist: Double,
    zDist: Double,
    private val sprites: SpriteSet,
) : SingleQuadParticle(clientWorld, x, y, z, 0.0, 0.0, 0.0, sprites.first()) {
    init {
        setPos(
            x + xDist * (random.nextDouble() * 2 - 1),
            y + yDist * (random.nextDouble() * 2 - 1),
            z + zDist * (random.nextDouble() * 2 - 1),
        )
        xo = x
        yo = y
        zo = z

        friction = 0.96f
        yd = 0.0
        xd *= 5.0
        zd *= 5.0

        val col = 1.0f - random.nextFloat() * 0.3f
        rCol = col
        gCol = col
        bCol = col

        quadSize *= 1.875f
        hasPhysics = false
        setSpriteFromAge(sprites)

        val baseLifetime = (8.0 / (random.nextFloat() * 0.8 + 0.3)).toInt()
        lifetime = max(baseLifetime * 2.5f, 1.0f).toInt()
    }

    override fun getLayer(): Layer = Layer.TRANSLUCENT

    override fun getQuadSize(a: Float): Float = quadSize * Mth.clamp((age + a) / lifetime * 32.0f, 0.0f, 1.0f)

    override fun tick() {
        super.tick()
        if (removed) return
        setSpriteFromAge(sprites)
    }

    class Factory(
        private val spriteProvider: SpriteSet,
    ) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            simpleParticleType: SimpleParticleType,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xDist: Double,
            yDist: Double,
            zDist: Double,
            random: RandomSource,
        ): Particle = CloudTorusParticle(clientWorld, x, y, z, xDist, yDist, zDist, spriteProvider)
    }
}