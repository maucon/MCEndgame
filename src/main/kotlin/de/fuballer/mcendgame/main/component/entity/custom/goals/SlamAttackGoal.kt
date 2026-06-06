package de.fuballer.mcendgame.main.component.entity.custom.goals

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomPosesEntity
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.SlamAttacker
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.pathfinder.Path
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.max

class SlamAttackGoal<T>(
    private val mob: T,
    private val moveSpeedFactor: Double,
    private val slamDuration: Int,
    private val slamImpactTime: Int,
    private val slamCooldown: Int,
) : Goal() where T : Mob, T : SlamAttacker {
    private var path: Path? = null
    private var targetX = 0.0
    private var targetY = 0.0
    private var targetZ = 0.0
    private var updateCountdownTicks = 0
    private var cooldown = 0
    private var lastUpdateTime = 0L
    private var slamTime = -1

    init {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK))
    }

    override fun canUse(): Boolean {
        val time = mob.level().gameTime
        if (time - lastUpdateTime < 20) return false
        lastUpdateTime = time

        val target = mob.target ?: return false
        if (!target.isAlive) return false

        path = mob.navigation.createPath(target, 0)
        return path != null || mob.isWithinMeleeAttackRange(target)
    }

    override fun canContinueToUse(): Boolean {
        if (slamTime >= 0) return true

        val target = mob.target ?: return false
        if (!target.isAlive) return false

        if (!mob.isWithinHome(target.blockPosition())) return false
        return target !is Player || (!target.isSpectator && !target.isCreative)
    }

    override fun start() {
        mob.navigation.moveTo(path, moveSpeedFactor)
        mob.setAggressive(true)
        updateCountdownTicks = 0
    }

    override fun stop() {
        val target = mob.target ?: return
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            mob.target = null
        }

        mob.setAggressive(false)
        mob.navigation.stop()
    }

    override fun requiresUpdateEveryTick() = true

    override fun tick() {
        val target = mob.target ?: return
        update(target)
        trySlam(target)
    }

    private fun update(
        target: LivingEntity
    ) {
        if (!updateSlam()) {
            mob.lookControl.setLookAt(target, 30.0f, 30.0f)
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

        if (!mob.navigation.moveTo(target, moveSpeedFactor)) {
            updateCountdownTicks += 15
        }

        updateCountdownTicks = adjustedTickDelay(updateCountdownTicks)
    }

    private fun shouldUpdate(
        target: LivingEntity
    ): Boolean {
        if (updateCountdownTicks > 0) return false
        if (!mob.sensing.hasLineOfSight(target)) return false

        if (targetX == 0.0 && targetY == 0.0 && targetZ == 0.0) return true
        if (target.position().distanceTo(Vec3(targetX, targetY, targetZ)) > 1) return true
        if (mob.navigation.isDone && mob.distanceTo(target) > 1) return true

        return mob.random.nextFloat() < 0.05
    }

    private fun updateSlam(): Boolean {
        if (slamTime < 0) return false

        mob.navigation.stop()

        slamTime++

        testSlamDamage()
        if (slamTime < slamDuration) return true

        updateCountdownTicks = adjustedTickDelay(5)
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
        cooldown = adjustedTickDelay(slamCooldown)
    }

    private fun canSlam(target: LivingEntity) =
        cooldown <= 0 && mob.isWithinMeleeAttackRange(target) && mob.sensing.hasLineOfSight(target)
}