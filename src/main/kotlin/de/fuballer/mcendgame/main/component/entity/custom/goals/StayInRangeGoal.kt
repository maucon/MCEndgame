package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.max

class StayInRangeGoal(
    private val entity: MobEntity,
    private val moveSpeedFactor: Double,
    maxDistance: Double,
) : DisableAbleGoal() {
    private var squaredMaxDistance = maxDistance * maxDistance
    private var path: Path? = null
    private var updateCountdownTicks = 0
    private var targetX = 0.0
    private var targetY = 0.0
    private var targetZ = 0.0

    init {
        setControls(EnumSet.of(Control.MOVE, Control.LOOK))
    }

    override fun canStart(): Boolean {
        if (isDisabled) return false
        val target = entity.target ?: return false
        if (!target.isAlive) return false

        path = entity.navigation.findPathTo(target, 0)
        return path != null || entity.squaredDistanceTo(target) < squaredMaxDistance
    }

    override fun shouldContinue(): Boolean {
        if (isDisabled) return false
        val target = entity.target ?: return false
        if (!target.isAlive) return false

        if (!entity.isInWalkTargetRange(target.blockPos)) return false
        return target !is PlayerEntity || (!target.isSpectator && !target.isCreative)
    }

    override fun start() {
        entity.navigation.startMovingAlong(path, moveSpeedFactor)
        entity.isAttacking = true
        updateCountdownTicks = 0
    }

    override fun stop() {
        val target = entity.target
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
            entity.target = null
        }

        entity.isAttacking = false
        entity.navigation.stop()
    }

    override fun tick() {
        val target = entity.target ?: return
        update(target)
    }

    private fun update(
        target: LivingEntity
    ) {
        entity.lookControl.lookAt(target, 30.0f, 30.0f)

        updateCountdownTicks = max(updateCountdownTicks - 1, 0)

        val squaredDistance = entity.squaredDistanceTo(target)
        if (squaredDistance < squaredMaxDistance) {
            entity.navigation.stop()
        }

        if (!shouldUpdateMovement(target)) return

        targetX = target.x
        targetY = target.y
        targetZ = target.z

        updateCountdownTicks = 4 + entity.random.nextInt(7)

        updateCountdownTicks += (squaredDistance / 20).toInt()

        if (!entity.navigation.startMovingTo(target, moveSpeedFactor)) {
            updateCountdownTicks += 15
        }

        updateCountdownTicks = getTickCount(updateCountdownTicks)
    }

    private fun shouldUpdateMovement(
        target: LivingEntity
    ): Boolean {
        if (updateCountdownTicks > 0) return false
        if (!entity.visibilityCache.canSee(target)) return false


        if (targetX == 0.0 && targetY == 0.0 && targetZ == 0.0) return true
        val isInRange = entity.squaredDistanceTo(target) < squaredMaxDistance
        if (target.pos.squaredDistanceTo(Vec3d(targetX, targetY, targetZ)) > 1 && !isInRange) return true
        if (entity.navigation.isIdle && !isInRange) return true

        return false
    }

    fun setMaxDistance(maxDistance: Double) {
        squaredMaxDistance = maxDistance * maxDistance
    }
}