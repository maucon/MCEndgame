package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.DemonkinEmpowerParticleEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.RandomSource
import java.util.*
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class DemonkinEmpowerParticle(
    clientWorld: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    targetId: UUID,
    duration: Int,
    sprite: TextureAtlasSprite,
    isFlame: Boolean,
) : MoveToTargetParticle(clientWorld, x, y, z, targetId, duration, sprite) {
    companion object {
        const val SIN_AMPLITUDE = 0.1
        const val SIN_FREQUENCY = 0.3
    }

    init {
        hasPhysics = false

        quadSize = 0.15F + 0.1F * Random.nextFloat()

        if (!isFlame) {
            val col = random.nextFloat() * 0.3f
            rCol = col
            gCol = col
            bCol = col
        }
    }

    val sinDirection = if (Random.nextBoolean()) 1.0 else -1.0

    override fun updateVelocityTowardTarget() {
        if (target == null) return

        val wave = sin(age * SIN_FREQUENCY) * SIN_AMPLITUDE

        val targetPos = target.boundingBox.center
        val dx = targetPos.x - x
        val dy = targetPos.y - y
        val dz = targetPos.z - z

        val length = sqrt(dx * dx + dz * dz).coerceAtLeast(0.001)
        val xSinOffsetFactor = dz / length
        val zSinOffsetFactor = dx / length

        val ticksLeft = (lifetime - age).coerceAtLeast(1)
        xd = dx / ticksLeft + wave * xSinOffsetFactor * sinDirection
        yd = dy / ticksLeft + wave * sinDirection
        zd = dz / ticksLeft + wave * zSinOffsetFactor * sinDirection
    }

    override fun getQuadSize(tickProgress: Float): Float {
        return quadSize
    }

    class Factory(
        private val spriteProvider: SpriteSet,
    ) : ParticleProvider<DemonkinEmpowerParticleEffect> {
        override fun createParticle(
            particleEffect: DemonkinEmpowerParticleEffect,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: RandomSource,
        ): Particle {
            var sprite = spriteProvider.first()
            var isFlame = true
            if (random.nextDouble() > 0.2) {
                val index = random.nextIntBetweenInclusive(1, 8)
                sprite = spriteProvider.get(index, 8)
                isFlame = false
            }
            return DemonkinEmpowerParticle(
                clientWorld,
                x,
                y,
                z,
                particleEffect.targetEntityId,
                particleEffect.duration,
                sprite,
                isFlame,
            )
        }
    }
}