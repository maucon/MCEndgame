package de.fuballer.mcendgame.main.component.entity.custom.attack.damage

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil.takeKnockbackFrom
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getDistanceToGround
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setShieldsCooldown
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Avatar
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.*
import kotlin.random.Random

class AreaAttackDamage(
    damageFactor: Float,
    knockbackFactor: Double,
    private val area: DamageArea,
    private val applyScale: Boolean = true,
    private val knockbackType: KnockbackType = KnockbackType.DAMAGER_CENTER,
    blockable: Boolean = true,
    disableBlockingShield: Float = 0.0F,
    knockbackWhenBlocked: Boolean = false,
) : AttackDamage(damageFactor, knockbackFactor, blockable, disableBlockingShield, knockbackWhenBlocked) {
    private var createParticles: Boolean = false
    private var particleCount: Int = 0
    private var particleHeightOffset: Double = 0.0
    private var particleType: SimpleParticleType = ParticleTypes.CRIT
    private var particleSpeed: Double = 0.0

    private var playSound: Boolean = false
    private var soundRequiresHit: Boolean = true
    private var sound: SoundEvent = SoundEvents.GENERIC_EXPLODE.value()
    private var pitch: Float = 1F
    private var volume: Float = 1F

    override fun apply(world: ServerLevel, damager: Mob, target: LivingEntity?): Boolean {
        applyAtOtherEntity(world, damager, damager)
        return true
    }

    fun applyAtOtherEntity(world: ServerLevel, damager: Mob, other: Entity) {
        val yRot = if (other is LivingEntity) other.yBodyRot else other.yRot
        val forward = other.calculateViewVector(other.xRot, yRot).horizontal().normalize()
        val sideways = forward.cross(Vec3(0.0, 1.0, 0.0))

        val scale = getScale(damager)

        // debug
        area.renderOutline(world, other, forward, sideways, scale)

        val targets = getTargets(world, other, scale).filter {
            area.intersects(it, other, forward, sideways, scale)
        }

        val slamCenter = area.getCenter(other, scale, forward, sideways)

        dealDamage(targets, damager, scale, forward, slamCenter)

        if (createParticles) createParticles(world, slamCenter, forward, sideways, scale)

        if (!playSound) return
        if (soundRequiresHit && targets.isEmpty()) return
        playSound(world, slamCenter, scale)
    }

    private fun getScale(damager: Mob) = if (applyScale) damager.getAttributeValue(Attributes.SCALE) else 1.0

    private fun getTargets(
        world: ServerLevel,
        originEntity: Entity,
        scale: Double,
    ): List<LivingEntity> {
        val box = area.getAxisAlignedBox(originEntity, scale)
        return world.getEntitiesOfClass(LivingEntity::class.java, box) { it != originEntity }
    }

    private fun dealDamage(
        targets: List<LivingEntity>,
        damager: Mob,
        scale: Double,
        forward: Vec3,
        slamCenter: Vec3,
    ) {
        val damage = getDamage(damager)
        val knockback = getKnockback(damager)

        targets.forEach {
            val dealtDamage = it.dealGenericAttackDamage(damage, damager, blockable)
            if (disableBlockingShield > 0 && it is Avatar && it.isBlocking) it.setShieldsCooldown(disableBlockingShield)
            if (dealtDamage || knockbackWhenBlocked) applyKnockback(it, damager, knockback, scale, forward, slamCenter)
        }
    }

    // TODO
    //  this currently is not capable of applying knockback from a secondary entities position
    //  AREA_CENTER works and is currently the only used for that case
    private fun applyKnockback(
        target: LivingEntity,
        damager: LivingEntity,
        knockback: Double,
        scale: Double,
        forward: Vec3,
        slamCenter: Vec3,
    ) {
        val knockBackStrength = knockback * if (applyScale) scale else 1.0
        target.needsSync = true
        target.hurtMarked = true

        when (knockbackType) {
            KnockbackType.FACING -> target.takeKnockbackFrom(damager, knockBackStrength, -forward.x, -forward.z)

            KnockbackType.AREA_CENTER -> {
                val knockbackDirection = target.position().subtract(slamCenter).normalize()
                target.takeKnockbackFrom(damager, knockBackStrength, -knockbackDirection.x, -knockbackDirection.z)
            }

            KnockbackType.DAMAGER_CENTER -> {
                val knockbackDirection = target.position().subtract(damager.position()).normalize()
                target.takeKnockbackFrom(damager, knockBackStrength, -knockbackDirection.x, -knockbackDirection.z)
            }
        }
    }

    fun setParticles(
        count: Int,
        heightOffset: Double,
        type: SimpleParticleType,
        speed: Double,
    ): AreaAttackDamage {
        createParticles = true
        particleCount = count
        particleHeightOffset = heightOffset
        particleType = type
        particleSpeed = speed

        return this
    }

    private fun createParticles(
        world: ServerLevel,
        slamCenter: Vec3,
        forward: Vec3,
        sideways: Vec3,
        scale: Double,
    ) {
        val scaledParticleCount = (particleCount * scale).toInt()
        repeat(scaledParticleCount) { createParticle(world, slamCenter, forward, sideways, scale) }
    }

    private fun createParticle(
        world: ServerLevel,
        slamCenter: Vec3,
        forward: Vec3,
        sideways: Vec3,
        scale: Double,
    ) {
        val forwardRandomOffset = area.getRandomForwardPos(scale)
        val sidewaysRandomOffset = area.getRandomSidewaysPos(scale)

        val particlePos = slamCenter.add(forward.scale(forwardRandomOffset)).add(sideways.scale(sidewaysRandomOffset))
        world.sendParticles(particleType, particlePos.x, particlePos.y + particleHeightOffset, particlePos.z, 1, 0.0, 0.0, 0.0, particleSpeed)
    }

    fun setSound(
        requiresHit: Boolean,
        sound: SoundEvent,
        pitch: Float,
        volume: Float,
    ): AreaAttackDamage {
        playSound = true
        soundRequiresHit = requiresHit
        this.sound = sound
        this.pitch = pitch
        this.volume = volume

        return this
    }

    private fun playSound(
        world: ServerLevel,
        slamCenter: Vec3,
        scale: Double,
    ) {
        val scaledVolume = min(volume * scale.toFloat(), 2F)

        world.playSound(
            null,
            slamCenter.x,
            slamCenter.y,
            slamCenter.z,
            sound,
            SoundSource.HOSTILE,
            scaledVolume,
            pitch
        )
    }

    override fun requiresTarget() = false

    class DamageArea(
        private val forwardRange: Double, // only forward
        private val sideRange: Double, // left & right -> 2* sideRange
        private val heightRange: Double, // up & down -> 2* heightRange
        private val forwardOffset: Double = 0.0, // positive -> forward
        private val sideOffset: Double = 0.0, // positive -> right
        private val heightOffset: Double = 0.0, // positive -> up
        private val offsetToGround: Boolean = false,
    ) {
        fun intersects(
            entity: LivingEntity,
            originEntity: Entity,
            forward: Vec3,
            sideways: Vec3,
            scale: Double
        ): Boolean {
            val bb = entity.boundingBox

            val xStep = (bb.maxX - bb.minX) / 2.0
            val yStep = (bb.maxY - bb.minY) / 2.0
            val zStep = (bb.maxZ - bb.minZ) / 2.0

            val distanceToGround = if (offsetToGround) originEntity.getDistanceToGround() else 0.0

            for (x in 0..2) {
                for (y in 0..2) {
                    for (z in 0..2) {
                        val point = Vec3(
                            bb.minX + x * xStep,
                            bb.minY + y * yStep,
                            bb.minZ + z * zStep
                        )

                        val areaOrigin = if (!offsetToGround) originEntity.position() else originEntity.position().subtract(0.0, distanceToGround, 0.0)
                        val relativePoint = point.subtract(areaOrigin)

                        if (contains(relativePoint, forward, sideways, scale)) return true
                    }
                }
            }

            return false
        }

        private fun contains(
            relativePos: Vec3,
            forward: Vec3,
            sideways: Vec3,
            scale: Double,
        ): Boolean {
            val forwardDistance = relativePos.dot(forward)
            val sidewaysDistance = relativePos.dot(sideways)
            val heightDistance = relativePos.y

            val maxForward = (forwardRange + forwardOffset) * scale
            val minForward = (forwardOffset) * scale
            if (forwardDistance !in minForward..maxForward) return false

            val maxSide = (sideRange + sideOffset) * scale
            val minSide = (-sideRange + sideOffset) * scale
            if (sidewaysDistance !in minSide..maxSide) return false

            val maxHeight = (heightRange + heightOffset) * scale
            val minHeight = (-heightRange + heightOffset) * scale
            if (heightDistance !in minHeight..maxHeight) return false

            return true
        }

        fun getCenter(
            originEntity: Entity,
            scale: Double,
            forward: Vec3,
            sideways: Vec3,
        ): Vec3 {
            var center = originEntity.position()

            val forwardCenter = (forwardOffset + forwardRange / 2) * scale
            center = center.add(forward.scale(forwardCenter))

            val sidewaysCenter = sideOffset * scale
            center = center.add(sideways.scale(sidewaysCenter))

            if (offsetToGround) center = center.subtract(0.0, originEntity.getDistanceToGround(), 0.0)

            return center
        }

        fun getAxisAlignedBox(
            originEntity: Entity,
            scale: Double,
        ): AABB {
            val hD = getMaxHorizontalDistance(scale)
            val x = originEntity.x
            var y = originEntity.y + heightOffset
            if (offsetToGround) y -= originEntity.getDistanceToGround()
            val z = originEntity.z
            return AABB(x - hD, y - heightRange - 3, z - hD, x + hD, y + heightRange, z + hD) // -3 accounts for height of most mobs
        }

        private fun getMaxHorizontalDistance(scale: Double): Double {
            val maxForward = max(forwardRange + forwardOffset, -forwardOffset)
            val maxSideways = sideRange + abs(sideOffset)
            val distance = sqrt(maxForward * maxForward + maxSideways * maxSideways)
            return distance * scale
        }

        fun getRandomForwardPos(scale: Double) = (forwardRange / 2 * Random.nextDouble().pow(2) * if (Random.nextBoolean()) 1 else -1) * scale
        fun getRandomSidewaysPos(scale: Double) = (sideRange * Random.nextDouble().pow(2) * if (Random.nextBoolean()) 1 else -1) * scale

        // for debugging / dev
        fun renderOutline(
            world: ServerLevel,
            originEntity: Entity,
            forward: Vec3,
            sideways: Vec3,
            scale: Double,
        ) {
            var origin = originEntity.position()
            if (offsetToGround) origin = origin.subtract(0.0, originEntity.getDistanceToGround(), 0.0)

            val minForward = forwardOffset * scale
            val maxForward = (forwardOffset + forwardRange) * scale

            val minSide = (-sideRange + sideOffset) * scale
            val maxSide = (sideRange + sideOffset) * scale

            val minHeight = (-heightRange + heightOffset) * scale
            val maxHeight = (heightRange + heightOffset) * scale

            fun point(f: Double, s: Double, h: Double): Vec3 {
                return origin
                    .add(forward.scale(f))
                    .add(sideways.scale(s))
                    .add(0.0, h, 0.0)
            }

            val corners = arrayOf(
                point(minForward, minSide, minHeight),
                point(minForward, maxSide, minHeight),
                point(maxForward, minSide, minHeight),
                point(maxForward, maxSide, minHeight),
                point(minForward, minSide, maxHeight),
                point(minForward, maxSide, maxHeight),
                point(maxForward, minSide, maxHeight),
                point(maxForward, maxSide, maxHeight),
            )

            val edges = arrayOf(
                0 to 1,
                0 to 2,
                1 to 3,
                2 to 3,

                4 to 5,
                4 to 6,
                5 to 7,
                6 to 7,

                0 to 4,
                1 to 5,
                2 to 6,
                3 to 7,
            )

            for ((a, b) in edges) {
                renderLine(world, corners[a], corners[b])
            }
        }

        private fun renderLine(
            world: ServerLevel,
            from: Vec3,
            to: Vec3,
        ) {
            val particle = ParticleTypes.END_ROD
            val distance = from.distanceTo(to)
            val steps = (distance * 8).toInt().coerceAtLeast(1)
            for (i in 0..steps) {
                val t = i.toDouble() / steps

                val pos = from.lerp(to, t)

                world.sendParticles(
                    particle,
                    pos.x,
                    pos.y,
                    pos.z,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }

    enum class KnockbackType {
        FACING,
        DAMAGER_CENTER,
        AREA_CENTER,
    }
}