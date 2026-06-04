package de.fuballer.mcendgame.main.component.entity.custom.attack

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.DelayedAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.instance.LeapAttackDamageInstance
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundData
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

open class LeapAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    damage: List<DelayedAttackDamage>,
    private val leapType: LeapType,
    sounds: List<DelayedSoundData> = listOf(),
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, damage, sounds, blockMovementDuration) where T : Mob, T : GeoEntity {
    constructor(
        animationData: AttackAnimationData,
        totalDuration: Int,
        cooldown: Int,
        trigger: TriggerCondition,
        damage: DelayedAttackDamage?,
        leapType: LeapType,
        sounds: List<DelayedSoundData> = listOf(),
        blockMovementDuration: Int = 0,
    ) : this(
        animationData,
        totalDuration,
        cooldown,
        trigger,
        if (damage != null) listOf(damage) else listOf(),
        leapType,
        sounds,
        blockMovementDuration,
    )

    override fun start(
        attacker: T,
        target: LivingEntity?
    ) {
        super.start(attacker, target)

        val existingTarget = target ?: return
        attacker.lookAt(existingTarget, 90F, 90F)
        attacker.lookControl.setLookAt(existingTarget)
        attacker.yBodyRot = attacker.yRot

        val distanceVector = existingTarget.position().subtract(attacker.position())
        val newVelocity = leapType.calculateVelocity(distanceVector)
        attacker.setDeltaMovement(newVelocity)
        attacker.needsSync = true

        val blockAbleMovementMob = attacker as? BlockAbleMovementMob<*> ?: return
        blockAbleMovementMob.setAirborneBlocked()
    }

    override fun getDamageInstance(
        target: LivingEntity?,
        delayedDamage: DelayedAttackDamage,
    ) = LeapAttackDamageInstance(delayedDamage.minDelay, delayedDamage.maxDelay, target, delayedDamage.damage)

    enum class LeapType(
        private val horizontalDistanceVelocityFactor: ((Double) -> Double),
        private val verticalDistanceVelocityFactor: ((Double) -> Double),
        private val additionalYVelocity: Double,
    ) {
        BASIC(
            { distance -> abs(distance * 0.2) },
            { distance -> sqrt(abs(0.16 * distance)) },
            0.3,
        ),
        BACKFLIP(
            { _ -> -1.0 },
            { _ -> 0.0 },
            0.6,
        ),
        JUMP_BACK(
            { _ -> -2.0 },
            { _ -> 0.0 },
            0.4,
        );

        fun calculateVelocity(distanceVector: Vec3): Vec3 {
            val direction = distanceVector.normalize()

            val horizontalDistance = distanceVector.horizontalDistance()
            val horizontalFactor = horizontalDistanceVelocityFactor.invoke(horizontalDistance)
            val verticalDistance = distanceVector.y
            val verticalFactor = verticalDistanceVelocityFactor.invoke(verticalDistance)

            val distanceScaledVelocity = Vec3(direction.x * horizontalFactor, max(direction.y * verticalFactor, 0.0), direction.z * horizontalFactor)

            val finalVelocity = distanceScaledVelocity.add(0.0, additionalYVelocity, 0.0)
            return finalVelocity
        }
    }
}