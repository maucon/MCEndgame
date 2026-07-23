package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil.takeKnockbackFrom
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.min

interface SlamAttacker : CustomPosesEntity {
    val slamAttacker: Mob
    val slamRadius: Double
    val minSlamStrength: Double
    val slamCenterFacingOffset: Double
    val applyScale: Boolean
    val knockbackStrength: Double

    fun shouldDamage(target: LivingEntity): Boolean

    fun slam() {
        val world = slamAttacker.level() as? ServerLevel ?: return

        val scale = if (applyScale) slamAttacker.getAttributeValue(Attributes.SCALE) else 1.0
        val scaledRadius = slamRadius * scale
        val scaledKnockbackStrength = knockbackStrength * scale
        val scaledOffset = slamCenterFacingOffset * scale

        val offset = slamAttacker.lookAngle.normalize().scale(scaledOffset)
        val damageCenter = slamAttacker.position().add(offset)

        val box = AABB(damageCenter, Vec3.ZERO).inflate(scaledRadius)
        val targets = world.getEntitiesOfClass(LivingEntity::class.java, box) { it != slamAttacker && shouldDamage(it) }
            .filter { damageCenter.distanceTo(it.position()) <= scaledRadius }

        damageTargets(
            targets,
            world,
            damageCenter,
            scaledRadius,
            scaledKnockbackStrength
        )

        createParticles(world, damageCenter, scaledRadius)
        playSound(world, damageCenter, scaledRadius)
    }

    private fun damageTargets(
        targets: List<LivingEntity>,
        world: ServerLevel,
        damageCenter: Vec3,
        scaledRadius: Double,
        scaledKnockbackStrength: Double,
    ) {
        val attackDamage = slamAttacker.getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()

        for (target in targets) {
            val distanceVector = target.position().subtract(damageCenter)
            val distancePercent = max(1 - (distanceVector.length() / scaledRadius), 0.0)

            val damage = getDistanceScaled(attackDamage.toDouble(), distancePercent).toFloat()
            target.dealGenericAttackDamage(damage, slamAttacker)

            val effectiveKnockbackStrength = getDistanceScaled(scaledKnockbackStrength, distancePercent)
            target.takeKnockbackFrom(slamAttacker, effectiveKnockbackStrength, -distanceVector.x, -distanceVector.z)
        }
    }

    private fun getDistanceScaled(
        value: Double,
        distancePercent: Double,
    ): Double {
        val min = value * minSlamStrength
        val range = value * (1 - minSlamStrength)
        return (min + range * distancePercent)
    }

    private fun createParticles(
        world: ServerLevel,
        center: Vec3,
        scaledRadius: Double,
    ) {
        world.sendParticles(
            ParticleTypes.CLOUD,
            center.x,
            center.y + 0.2,
            center.z,
            (15 * scaledRadius).toInt(),
            scaledRadius * 0.5,
            0.1,
            scaledRadius * 0.5,
            0.01
        )
    }

    private fun playSound(
        world: ServerLevel,
        slamCenter: Vec3,
        slamRadius: Double,
    ) {
        val volume = min(slamRadius / 3, 2.0).toFloat()
        world.playSound(
            null,
            slamCenter.x,
            slamCenter.y,
            slamCenter.z,
            SoundEvents.GENERIC_EXPLODE,
            SoundSource.HOSTILE,
            volume,
            1F
        )
    }
}