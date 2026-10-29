package de.fuballer.mcendgame.main.component.entity.custom.attack.damage

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil.takeKnockbackFrom
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealing.dealGenericAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.damage_area.DamageArea
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.status_effect.StatusEffectData
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
import net.minecraft.world.phys.Vec3
import kotlin.math.min

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

    private var applyStatusEffects: Boolean = false
    private var statusEffects: List<StatusEffectData> = listOf()

    override fun apply(world: ServerLevel, damager: Mob, target: LivingEntity?): Boolean {
        applyAtEntity(world, damager, damager)
        return true
    }

    fun applyRotated(world: ServerLevel, damager: Mob, rad: Float) {
        applyAtEntity(world, damager, damager, rad)
    }

    fun applyAtEntity(
        world: ServerLevel,
        damager: Mob,
        at: Entity,
        extraRotRad: Float = 0F,
    ) {
        val yRot = if (at is LivingEntity) at.yBodyRot else at.yRot
        val forward = Entity
            .calculateViewVector(at.xRot, yRot)
            .horizontal()
            .yRot(extraRotRad)
            .normalize()
        val sideways = forward.cross(Vec3(0.0, 1.0, 0.0))

        val scale = getScale(damager)

        // debug
        area.renderOutline(world, at, forward, sideways, scale)

        val targets = getTargets(world, at, scale, forward, sideways).filter {
            area.intersects(it, at, forward, sideways, scale)
        }

        val slamCenter = area.getCenter(at, scale, forward, sideways)

        val damagedTargets = dealDamage(targets, damager, scale, forward, sideways, slamCenter)

        if (createParticles) createParticles(world, at, forward, sideways, scale)

        val hitTargets = targets.isNotEmpty()
        if (playSound && !(soundRequiresHit && !hitTargets)) playSound(world, slamCenter, scale)
        if (applyStatusEffects && hitTargets) applyStatusEffects(damagedTargets)
    }

    private fun getScale(damager: Mob) = if (applyScale) damager.getAttributeValue(Attributes.SCALE) else 1.0

    private fun getTargets(
        world: ServerLevel,
        originEntity: Entity,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): List<LivingEntity> {
        val box = area.getAxisAlignedBoxApproximation(originEntity, scale, forward, sideways)
        return world.getEntitiesOfClass(LivingEntity::class.java, box) { it != originEntity }
    }

    private fun dealDamage(
        targets: List<LivingEntity>,
        damager: Mob,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
        slamCenter: Vec3,
    ): List<LivingEntity> {
        val damage = getDamage(damager)
        val knockback = getKnockback(damager)

        val damagedTargets = mutableListOf<LivingEntity>()
        targets.forEach {
            val dealtDamage = it.dealGenericAttackDamage(damage, damager, blockable)
            if (dealtDamage) damagedTargets.add(it)
            if (disableBlockingShield > 0 && it is Avatar && it.isBlocking) it.setShieldsCooldown(disableBlockingShield)
            if (dealtDamage || knockbackWhenBlocked) applyKnockback(it, damager, knockback, scale, forward, sideways, slamCenter)
        }
        return damagedTargets
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
        sideways: Vec3,
        slamCenter: Vec3,
    ) {
        val knockBackStrength = knockback * if (applyScale) scale else 1.0
        target.needsSync = true
        target.syncVelocity = true

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

            KnockbackType.BEASTWEAVER_RHINO_CHARGE -> {
                val toTarget = target.position().subtract(damager.position()).normalize()
                val cross = forward.x * toTarget.z - forward.z * toTarget.x
                val horizontalDirection = if (cross > 0) sideways else sideways.scale(-1.0)
                target.takeKnockbackFrom(damager, knockBackStrength, -horizontalDirection.x, -0.6, -horizontalDirection.z)
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
        at: Entity,
        forward: Vec3,
        sideways: Vec3,
        scale: Double,
    ) {
        val scaledParticleCount = (particleCount * scale).toInt()
        val positions = area.getRandomContainedXZPositions(scaledParticleCount, at, scale, forward, sideways)
        positions.forEach {
            world.sendParticles(particleType, it.x, it.y + particleHeightOffset, it.z, 1, 0.0, 0.0, 0.0, particleSpeed)
        }
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

    fun setStatusEffects(
        vararg effects: StatusEffectData,
    ): AreaAttackDamage {
        applyStatusEffects = true
        statusEffects = effects.toList()
        return this
    }

    private fun applyStatusEffects(targets: List<LivingEntity>) {
        targets.forEach { target ->
            statusEffects.forEach { effectData ->
                effectData.apply(target)
            }
        }
    }

    override fun requiresTarget() = false

    enum class KnockbackType {
        FACING,
        DAMAGER_CENTER,
        AREA_CENTER,
        BEASTWEAVER_RHINO_CHARGE,
    }
}