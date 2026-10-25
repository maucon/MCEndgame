package de.fuballer.mcendgame.client.component.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.LightCoordsUtil
import net.minecraft.util.RandomSource
import kotlin.math.pow
import kotlin.random.Random

open class ReverseFlameParticle protected constructor(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xd: Double,
    yd: Double,
    zd: Double,
    sprite: TextureAtlasSprite,
) : SingleQuadParticle(level, x, y, z, sprite) {
    private val xStart: Double
    private val yStart: Double
    private val zStart: Double

    init {
        this.xd = xd
        this.yd = yd
        this.zd = zd
        this.x = x
        this.y = y
        this.z = z
        xStart = this.x
        yStart = this.y
        zStart = this.z
        quadSize = 0.1F * (random.nextFloat() * 0.5F + 0.5F) * 2.0F
        lifetime = Random.nextInt(6, 7)
    }

    public override fun getLayer() = Layer.OPAQUE

    override fun move(xa: Double, ya: Double, za: Double) {
        boundingBox = boundingBox.move(xa, ya, za)
        setLocationFromBoundingbox()
    }

    override fun getQuadSize(partialTick: Float): Float {
        val progress = (age + partialTick) / lifetime
        val factor = 1F - ((1F - progress).pow(2))
        return quadSize * factor
    }

    public override fun getLightCoords(a: Float): Int {
        val brightness = (age.toFloat() / lifetime).pow(3)
        return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(a), brightness)
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            remove()
            return
        }

        var progress = age.toFloat() / lifetime
        val a = progress
        progress = -progress + progress * progress * 2.0f
        progress = 1.0f - progress
        x = xStart + xd * progress
        y = yStart + yd * progress + (1.0f - a)
        z = zStart + zd * progress
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
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: RandomSource,
        ): Particle {
            return ReverseFlameParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.get(random))
        }
    }
}