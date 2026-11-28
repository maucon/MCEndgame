package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.AbsorbBossPowerParticleEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.random.Random

class AbsorbBossPowerParticle(
    clientWorld: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    targetId: UUID,
    sprite: TextureAtlasSprite,
    isFlame: Boolean,
) : SingleQuadParticle(clientWorld, x, y, z, 0.0, 0.0, 0.0, sprite) {
    val target: Entity? = clientWorld.getEntity(targetId)

    init {
        hasPhysics = false
        quadSize = 0.15F + 0.1F * Random.nextFloat()

        if (!isFlame) {
            val col = random.nextFloat() * 0.3f
            rCol = col
            gCol = col
            bCol = col
        }

        lifetime = 200

        xd = (random.nextDouble() - 0.5) * 0.8
        yd = (random.nextDouble() - 0.5) * 0.8
        zd = (random.nextDouble() - 0.5) * 0.8
    }

    override fun tick() {
        super.tick()

        if (target == null || !target.isAlive) {
            remove()
            return
        }

        testReachedTarget()

        updateVelocity()
    }

    fun updateVelocity() {
        if (target == null) return

        val targetPos = target.boundingBox.center

        val dx = targetPos.x - x
        val dy = targetPos.y - y
        val dz = targetPos.z - z

        val direction = Vec3(dx, dy, dz).normalize()

        val currentVelocity = Vec3(xd, yd, zd)
        val targetVelocity = direction.scale(0.6)

        val newVelocity = currentVelocity.lerp(targetVelocity, 0.1)

        xd = newVelocity.x
        yd = newVelocity.y
        zd = newVelocity.z
    }

    private fun testReachedTarget() {
        val targetPos = target!!.boundingBox.center
        val dx = targetPos.x - x
        val dy = targetPos.y - y
        val dz = targetPos.z - z

        val distanceSq = dx * dx + dy * dy + dz * dz
        if (distanceSq < 0.2) {
            remove()
            return
        }
    }

    override fun getQuadSize(tickProgress: Float) = quadSize

    override fun getLayer(): Layer = Layer.OPAQUE

    class Factory(
        private val spriteProvider: SpriteSet,
    ) : ParticleProvider<AbsorbBossPowerParticleEffect> {
        override fun createParticle(
            particleEffect: AbsorbBossPowerParticleEffect,
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
            return AbsorbBossPowerParticle(
                clientWorld,
                x,
                y,
                z,
                particleEffect.targetEntityId,
                sprite,
                isFlame,
            )
        }
    }
}