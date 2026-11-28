package de.fuballer.mcendgame.client.component.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.entity.Entity
import java.util.*

open class MoveToTargetParticle(
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

    open fun updateVelocityTowardTarget() {
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
}