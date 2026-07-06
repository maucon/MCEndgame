package de.fuballer.mcendgame.main.component.entity.custom.goals

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomPosesEntity
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.SlamAttacker
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.max

class SlamAttackGoal<T>(
    private val mob: T,
    private val moveSpeedFactor: Double,
    private val slamDuration: Int,
    private val slamImpactTime: Int,
    private val slamCooldown: Int,
) : Goal() where T : MobEntity, T : SlamAttacker {
    private var path: Path? = null
    private var targetX = 0.0
    private var targetY = 0.0
    private var targetZ = 0.0
    private var updateCountdownTicks = 0
    private var cooldown = 0
    private var lastUpdateTime = 0L
    private var slamTime = -1

    init {
        setControls(EnumSet.of(Control.MOVE, Control.LOOK))
    }

    override fun canStart(): Boolean {
        val time = mob.entityWorld.time
        if (time - lastUpdateTime < 20) return false
        lastUpdateTime = time

        val target = mob.target ?: return false
        if (!target.isAlive) return false

        path = mob.navigation.findPathTo(target, 0)
        return path != null || mob.isInAttackRange(target)
    }

    override fun shouldContinue(): Boolean {
        if (slamTime >= 0) return true

        val target = mob.target ?: return false
        if (!target.isAlive) return false

        if (!mob.isInPositionTargetRange(target.blockPos)) return false
        return target !is PlayerEntity || (!target.isSpectator && !target.isCreative)
    }

    override fun start() {
        mob.navigation.startMovingAlong(path, moveSpeedFactor)
        mob.isAttacking = true
        updateCountdownTicks = 0
    }

    override fun stop() {
        val target = mob.target
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
            mob.target = null
        }

        mob.isAttacking = false
        mob.navigation.stop()
    }

    override fun shouldRunEveryTick() = true

    override fun tick() {
        val target = mob.target ?: return
        update(target)
        trySlam(target)
    }

    private fun update(
        target: LivingEntity
    ) {
        if (!updateSlam()) {
            mob.lookControl.lookAt(target, 30.0f, 30.0f)
        }

        cooldown = max(cooldown - 1, 0)
        updateCountdownTicks = max(updateCountdownTicks - 1, 0)

        if (!shouldUpdate(target)) return

        targetX = target.x
        targetY = target.y
        targetZ = target.z

        updateCountdownTicks = 4 + mob.random.nextInt(7)

        val distance = mob.distanceTo(target)
        updateCountdownTicks += (distance / 10).toInt()

        if (!mob.navigation.startMovingTo(target, moveSpeedFactor)) {
            updateCountdownTicks += 15
        }

        updateCountdownTicks = getTickCount(updateCountdownTicks)
    }

    private fun shouldUpdate(
        target: LivingEntity
    ): Boolean {
        if (updateCountdownTicks > 0) return false
        if (!mob.visibilityCache.canSee(target)) return false

        if (targetX == 0.0 && targetY == 0.0 && targetZ == 0.0) return true
        if (target.pos.distanceTo(Vec3d(targetX, targetY, targetZ)) > 1) return true
        if (mob.navigation.isIdle && mob.distanceTo(target) > 1) return true

        return mob.random.nextFloat() < 0.05
    }

    private fun updateSlam(): Boolean {
        if (slamTime < 0) return false

        mob.navigation.stop()

        slamTime++

        testSlamDamage()
        if (slamTime < slamDuration) return true

        updateCountdownTicks = getTickCount(5)
        slamTime = -1
        mob.setPose(CustomPosesEntity.CustomPose.IDLING)

        return true
    }

    private fun testSlamDamage() {
        if (slamTime != slamImpactTime) return
        mob.slam()
    }

    private fun trySlam(target: LivingEntity) {
        if (!canSlam(target)) return

        slamTime = 0
        mob.setPose(CustomPosesEntity.CustomPose.SLAMMING)
        cooldown = getTickCount(slamCooldown)
    }

    private fun canSlam(target: LivingEntity) =
        cooldown <= 0 && mob.isInAttackRange(target) && mob.visibilityCache.canSee(target)
}