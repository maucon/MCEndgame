package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.pathfinder.Path
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.max

class StayInRangeGoal(
    private val entity: Mob,
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
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK))
    }

    override fun canUse(): Boolean {
        if (isDisabled) return false
        val target = entity.target ?: return false
        if (!target.isAlive) return false

        path = entity.navigation.createPath(target, 0)
        return path != null || entity.distanceToSqr(target) < squaredMaxDistance
    }

    override fun canContinueToUse(): Boolean {
        if (isDisabled) return false
        val target = entity.target ?: return false
        if (!target.isAlive) return false

        return target !is Player || (!target.isSpectator && !target.isCreative)
    }

    override fun start() {
        entity.navigation.moveTo(path, moveSpeedFactor)
        entity.setAggressive(true)
        updateCountdownTicks = 0
    }

    override fun stop() {
        val target = entity.target ?: return
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            entity.target = null
        }

        entity.setAggressive(false)
        entity.navigation.stop()
    }

    override fun tick() {
        val target = entity.target ?: return
        update(target)
    }

    private fun update(
        target: LivingEntity
    ) {
        entity.lookControl.setLookAt(target, 30.0f, 30.0f)

        updateCountdownTicks = max(updateCountdownTicks - 1, 0)

        val squaredDistance = entity.distanceToSqr(target)
        if (squaredDistance < squaredMaxDistance) {
            entity.navigation.stop()
        }

        if (!shouldUpdateMovement(target)) return

        targetX = target.x
        targetY = target.y
        targetZ = target.z

        updateCountdownTicks = 4 + entity.random.nextInt(7)

        updateCountdownTicks += (squaredDistance / 20).toInt()

        if (!entity.navigation.moveTo(target, moveSpeedFactor)) {
            updateCountdownTicks += 15
        }

        updateCountdownTicks = adjustedTickDelay(updateCountdownTicks)
    }

    private fun shouldUpdateMovement(
        target: LivingEntity
    ): Boolean {
        if (updateCountdownTicks > 0) return false
        if (!entity.sensing.hasLineOfSight(target)) return false

        if (targetX == 0.0 && targetY == 0.0 && targetZ == 0.0) return true
        val isInRange = entity.distanceToSqr(target) < squaredMaxDistance
        if (target.position().distanceToSqr(Vec3(targetX, targetY, targetZ)) > 1 && !isInRange) return true
        if (entity.navigation.isDone && !isInRange) return true

        return false
    }

    fun setMaxDistance(maxDistance: Double) {
        squaredMaxDistance = maxDistance * maxDistance
    }
}