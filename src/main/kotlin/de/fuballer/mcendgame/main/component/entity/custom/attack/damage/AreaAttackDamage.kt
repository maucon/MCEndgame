package de.fuballer.mcendgame.main.component.entity.custom.attack.damage

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil.takeKnockbackFrom
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setShieldsCooldown
import de.fuballer.mcendgame.main.util.extension.Vec3dExtension.horizontal
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
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
) : AttackDamage(damageFactor, knockbackFactor, blockable, disableBlockingShield) {
    private var createParticles: Boolean = false
    private var particleCount: Int = 0
    private var particleHeightOffset: Double = 0.0
    private var particleType: SimpleParticleType = ParticleTypes.CRIT
    private var particleSpeed: Double = 0.0

    private var playSound: Boolean = false
    private var soundRequiresHit: Boolean = true
    private var sound: SoundEvent = SoundEvents.ENTITY_GENERIC_EXPLODE.value()
    private var pitch: Float = 1F
    private var volume: Float = 1F

    override fun apply(world: ServerWorld, damager: MobEntity, target: LivingEntity?): Boolean {
        val forward = damager.getRotationVector(damager.pitch, damager.bodyYaw).horizontal().normalize()
        val sideways = forward.crossProduct(Vec3d(0.0, 1.0, 0.0))

        val scale = getScale(damager)

        val targets = getTargets(world, damager, scale).filter {
            area.contains(it.pos.subtract(damager.pos), forward, sideways, scale)
                    || area.contains(it.pos.add(0.0, it.height.toDouble(), 0.0).subtract(damager.pos), forward, sideways, scale)
        }

        val slamCenter = area.getCenter(damager, scale, forward, sideways)

        dealDamage(targets, damager, scale, forward, slamCenter)

        if (createParticles) createParticles(world, slamCenter, forward, sideways, scale)

        if (!playSound) return true
        if (soundRequiresHit && targets.isEmpty()) return true
        playSound(world, slamCenter, scale)

        return true
    }

    private fun getScale(damager: MobEntity) = if (applyScale) damager.getAttributeValue(EntityAttributes.GENERIC_SCALE) else 1.0

    private fun getTargets(
        world: ServerWorld,
        damager: LivingEntity,
        scale: Double,
    ): List<LivingEntity> {
        val box = area.getAxisAlignedBox(damager, scale)
        return world.getEntitiesByClass(LivingEntity::class.java, box) { it != damager }
    }

    private fun dealDamage(
        targets: List<LivingEntity>,
        damager: MobEntity,
        scale: Double,
        forward: Vec3d,
        slamCenter: Vec3d,
    ) {
        val damage = getDamage(damager)
        val knockback = getKnockback(damager)

        targets.forEach {
            it.dealGenericAttackDamage(damage, damager, blockable)
            if (disableBlockingShield > 0 && it is PlayerEntity && it.isBlocking) it.setShieldsCooldown(disableBlockingShield)
            applyKnockback(it, damager, knockback, scale, forward, slamCenter)
        }
    }

    private fun applyKnockback(
        target: LivingEntity,
        damager: LivingEntity,
        knockback: Double,
        scale: Double,
        forward: Vec3d,
        slamCenter: Vec3d,
    ) {
        val knockBackStrength = knockback * if (applyScale) scale else 1.0
        target.velocityDirty = true

        when (knockbackType) {
            KnockbackType.FACING -> target.takeKnockbackFrom(damager, knockBackStrength, -forward.x, -forward.z)

            KnockbackType.AREA_CENTER -> {
                val knockbackDirection = target.pos.subtract(slamCenter).normalize()
                target.takeKnockbackFrom(damager, knockBackStrength, -knockbackDirection.x, -knockbackDirection.z)
            }

            KnockbackType.DAMAGER_CENTER -> {
                val knockbackDirection = target.pos.subtract(damager.pos).normalize()
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
        world: ServerWorld,
        slamCenter: Vec3d,
        forward: Vec3d,
        sideways: Vec3d,
        scale: Double,
    ) {
        val scaledParticleCount = (particleCount * scale).toInt()
        for (i in 0 until scaledParticleCount) createParticle(world, slamCenter, forward, sideways, scale)
    }

    private fun createParticle(
        world: ServerWorld,
        slamCenter: Vec3d,
        forward: Vec3d,
        sideways: Vec3d,
        scale: Double,
    ) {
        val forwardRandomOffset = area.getRandomForwardPos(scale)
        val sidewaysRandomOffset = area.getRandomSidewaysPos(scale)

        val particlePos = slamCenter.add(forward.multiply(forwardRandomOffset)).add(sideways.multiply(sidewaysRandomOffset))
        world.spawnParticles(particleType, particlePos.x, particlePos.y + particleHeightOffset, particlePos.z, 1, 0.0, 0.0, 0.0, particleSpeed)
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
        world: ServerWorld,
        slamCenter: Vec3d,
        scale: Double,
    ) {
        val scaledVolume = min(volume * scale.toFloat(), 2F)

        world.playSound(
            null,
            slamCenter.x,
            slamCenter.y,
            slamCenter.z,
            sound,
            SoundCategory.HOSTILE,
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
    ) {
        fun contains(
            relativePos: Vec3d,
            forward: Vec3d,
            sideways: Vec3d,
            scale: Double,
        ): Boolean {
            val forwardDistance = relativePos.dotProduct(forward)
            val sidewaysDistance = relativePos.dotProduct(sideways)
            val heightDistance = relativePos.y

            val maxForward = (forwardRange + forwardOffset) * scale
            val minForward = (forwardOffset) * scale
            if (forwardDistance > maxForward || forwardDistance < minForward) return false

            val maxSide = (sideRange + sideOffset) * scale
            val minSide = (-sideRange + sideOffset) * scale
            if (sidewaysDistance > maxSide || sidewaysDistance < minSide) return false

            val maxHeight = (heightRange + heightOffset) * scale
            val minHeight = (-heightRange + heightOffset) * scale
            if (heightDistance > maxHeight || heightDistance < minHeight) return false

            return true
        }

        fun getCenter(
            damager: LivingEntity,
            scale: Double,
            forward: Vec3d,
            sideways: Vec3d,
        ): Vec3d {
            var center = damager.pos

            val forwardCenter = (forwardOffset + forwardRange / 2) * scale
            center = center.add(forward.multiply(forwardCenter))

            val sidewaysCenter = sideOffset * scale
            center = center.add(sideways.multiply(sidewaysCenter))

            return center
        }

        fun getAxisAlignedBox(
            damager: LivingEntity,
            scale: Double,
        ): Box {
            val hD = getMaxHorizontalDistance(scale)
            val x = damager.x
            val y = damager.y + heightOffset
            val z = damager.z
            return Box(x - hD, y - heightRange - 3, z - hD, x + hD, y + heightRange, z + hD) // -3 accounts for height of most mobs
        }

        private fun getMaxHorizontalDistance(scale: Double): Double {
            val maxForward = max(forwardRange + forwardOffset, -forwardOffset)
            val maxSideways = sideRange + abs(sideOffset)
            val distance = sqrt(maxForward * maxForward + maxSideways * maxSideways)
            return distance * scale
        }

        fun getRandomForwardPos(scale: Double) = (forwardRange / 2 * Random.nextDouble().pow(2) * if (Random.nextBoolean()) 1 else -1) * scale
        fun getRandomSidewaysPos(scale: Double) = (sideRange * Random.nextDouble().pow(2) * if (Random.nextBoolean()) 1 else -1) * scale

    }

    enum class KnockbackType {
        FACING,
        DAMAGER_CENTER,
        AREA_CENTER,
    }
}