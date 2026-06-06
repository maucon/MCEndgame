package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.MoveToTargetFlameParticleEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import java.util.*

class MoveToTargetFlameParticle(
    clientWorld: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    targetId: UUID,
    duration: Int,
    sprite: TextureAtlasSprite,
) : SingleQuadParticle(clientWorld, x, y, z, 0.0, 0.0, 0.0, sprite) {
    val target: Entity? = clientWorld.getEntity(targetId)

    init {
        lifetime = duration

        updateVelocityTowardTarget()
    }

    private fun updateVelocityTowardTarget() {
        if (target == null) return

        val targetPos = target.boundingBox.center

        val dx = targetPos.x - x
        val dy = targetPos.y - y
        val dz = targetPos.z - z

        val ticksLeft = (lifetime - age).coerceAtLeast(1)

        xd = dx / ticksLeft
        yd = dy / ticksLeft
        zd = dz / ticksLeft
    }

    override fun tick() {
        super.tick()

        if (target == null || !target.isAlive) {
            remove()
            return
        }

        testReachedTarget()

        updateVelocityTowardTarget()
    }

    private fun testReachedTarget() {
        val targetPos = target!!.boundingBox.center
        val dx = targetPos.x - x
        val dy = targetPos.y - y
        val dz = targetPos.z - z

        val distanceSq = dx * dx + dy * dy + dz * dz
        if (distanceSq < 0.04) {
            remove()
            return
        }
    }

    override fun getQuadSize(tickProgress: Float): Float {
        val time = (age + tickProgress) / lifetime.toFloat()
        return quadSize * (0.3f + time * 1.2f)
    }

    override fun getLayer(): Layer = Layer.OPAQUE

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