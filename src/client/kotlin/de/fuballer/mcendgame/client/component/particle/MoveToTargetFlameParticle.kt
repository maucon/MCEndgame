package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.MoveToTargetFlameParticleEffect
import net.minecraft.client.particle.BillboardParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.texture.Sprite
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import java.util.*

class MoveToTargetFlameParticle(
    clientWorld: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    targetId: UUID,
    duration: Int,
    sprite: Sprite,
) : BillboardParticle(clientWorld, x, y, z, 0.0, 0.0, 0.0, sprite) {
    val target: Entity? = clientWorld.getEntity(targetId)

    init {
        maxAge = duration

        updateVelocityTowardTarget()
    }

    private fun updateVelocityTowardTarget() {
        if (target == null) return

        val targetPos = target.boundingBox.center

        val dx = targetPos.x - x
        val dy = targetPos.y - y
        val dz = targetPos.z - z

        val ticksLeft = (maxAge - age).coerceAtLeast(1)

        velocityX = dx / ticksLeft
        velocityY = dy / ticksLeft
        velocityZ = dz / ticksLeft
    }

    override fun tick() {
        super.tick()

        if (target == null || !target.isAlive) {
            markDead()
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
            markDead()
            return
        }
    }

    override fun getSize(tickProgress: Float): Float {
        val time = (age + tickProgress) / maxAge.toFloat()
        return scale * (0.3f + time * 1.2f)
    }

    override fun getRenderType(): RenderType = RenderType.PARTICLE_ATLAS_OPAQUE

    class Factory(
        private val spriteProvider: SpriteProvider,
    ) : ParticleFactory<MoveToTargetFlameParticleEffect> {
        override fun createParticle(
            particleEffect: MoveToTargetFlameParticleEffect,
            clientWorld: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: net.minecraft.util.math.random.Random,
        ): Particle {
            return MoveToTargetFlameParticle(
                clientWorld,
                x,
                y,
                z,
                particleEffect.targetEntityId,
                particleEffect.duration,
                spriteProvider.getSprite(random)
            )
        }
    }
}