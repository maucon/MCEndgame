package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.monster.RangedAttackMob
import java.util.*

class KeepDistanceToTargetGoal<T>(
    private val entity: T,
    private val speed: Double,
    minDistance: Float,
    maxDistance: Float,
) : DisableAbleGoal() where  T : RangedAttackMob, T : Mob {
    private val squaredMinDistance: Float
    private val squaredMaxDistance: Float
    private var seeingTargetTicker = 0
    private var strafingClockwise = false
    private var strafingBackwards = false
    private var strafingTime = -1

    init {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK))

        squaredMinDistance = minDistance * minDistance
        squaredMaxDistance = maxDistance * maxDistance
    }

    override fun canUse(): Boolean {
        if (isDisabled) return false
        return entity.target != null
    }

    override fun canContinueToUse(): Boolean {
        if (isDisabled) return false
        if (entity.target != null) return true
        return !entity.navigation.isDone
    }

    override fun start() {
        super.start()
        entity.setAggressive(true)
    }

    override fun stop() {
        super.stop()
        entity.setAggressive(false)
        seeingTargetTicker = 0
        entity.moveControl.strafe(0F, 0F)
        entity.navigation.stop()
    }

    override fun requiresUpdateEveryTick() = true

    override fun tick() {
        val target = entity.target ?: return

        updateSeeingTargetTicker(target)
        val squaredDistanceToTarget = entity.distanceToSqr(target)
        if (squaredDistanceToTarget <= squaredMaxDistance && seeingTargetTicker >= 20) {
            entity.navigation.stop()
            strafingTime++
        } else {
            entity.navigation.moveTo(target, speed)
            strafingTime = -1
        }

        updateStrafing(target, squaredDistanceToTarget)
    }

    private fun updateSeeingTargetTicker(
        target: LivingEntity
    ): Boolean {
        val canSeeTarget = entity.sensing.hasLineOfSight(target)
        val sawTarget = seeingTargetTicker > 0

        if (canSeeTarget != sawTarget) seeingTargetTicker = 0
        seeingTargetTicker += if (canSeeTarget) 1 else -1

        return canSeeTarget
    }

    private fun updateStrafing(
        target: LivingEntity,
        squaredDistanceToTarget: Double,
    ) {
        if (strafingTime >= 20) {
            if (entity.getRandom().nextFloat() < 0.3) {
                strafingClockwise = !strafingClockwise
            }

            if (entity.getRandom().nextFloat() < 0.3) {
                strafingBackwards = !strafingBackwards
            }

            strafingTime = 0
        }

        if (strafingTime > -1) {
            if (squaredDistanceToTarget > squaredMaxDistance) {
                strafingBackwards = false
            } else if (squaredDistanceToTarget < squaredMinDistance) {
                strafingBackwards = true
            }

            val strafeBackwards = if (strafingBackwards) -0.5f else 0.5f
            val strafeClockwise = if (strafingClockwise) 0.5f else -0.5f
            entity.getMoveControl().strafe(strafeBackwards, strafeClockwise)
            (entity.controlledVehicle as? Mob)?.lookAt(target, 30.0f, 30.0f)

            entity.lookAt(target, 30.0f, 30.0f)
        } else {
            entity.getLookControl().setLookAt(target, 30.0f, 30.0f)
        }
    }
}