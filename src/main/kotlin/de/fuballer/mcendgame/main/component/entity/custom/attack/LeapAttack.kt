package de.fuballer.mcendgame.main.component.entity.custom.attack

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

open class LeapAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    data: List<DelayedAttackData>,
    private val leapType: LeapType,
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, data, blockMovementDuration) where T : Mob, T : GeoEntity {

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
        attacker.deltaMovement = newVelocity
        attacker.needsSync = true

        val blockAbleMovementMob = attacker as? BlockAbleMovementMob<*> ?: return
        blockAbleMovementMob.setAirborneBlocked()
    }

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