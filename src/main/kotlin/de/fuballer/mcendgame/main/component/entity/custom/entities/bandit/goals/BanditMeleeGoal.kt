package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.goals

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.pathfinder.Path
import java.util.*
import kotlin.math.max

open class BanditMeleeGoal(
    protected val banditEntity: BanditEntity,
    private val speedModifier: Double,
) : Goal() {
    private var path: Path? = null
    private var pathedTargetX = 0.0
    private var pathedTargetY = 0.0
    private var pathedTargetZ = 0.0
    private var ticksUntilNextPathRecalculation = 0
    protected var ticksUntilNextAttack: Int = 0
    protected val attackInterval: Int = adjustedTickDelay(20)
    private var lastCanUseCheck: Long = 0

    init {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK))
    }

    companion object {
        private const val COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L
    }

    override fun canUse(): Boolean {
        val time = banditEntity.level().gameTime
        if (time - lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) return false

        lastCanUseCheck = time
        val target = banditEntity.target ?: return false
        if (!target.isAlive) return false

        path = banditEntity.getNavigation().createPath(target, 0)
        if (path != null) return true
        return banditEntity.isWithinMeleeAttackRange(target)
    }

    override fun canContinueToUse(): Boolean {
        val target = banditEntity.target ?: return false
        if (!target.isAlive) return false

        if (!banditEntity.isWithinHome(target.blockPosition())) return false
        return !(target is Player && (target.isSpectator || target.isCreative))
    }

    override fun start() {
        banditEntity.getNavigation().moveTo(path, speedModifier)
        banditEntity.setAggressive(true)
        ticksUntilNextPathRecalculation = 0
        ticksUntilNextAttack = 0
    }

    override fun stop() {
        val target = banditEntity.target
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target!!)) banditEntity.target = null

        banditEntity.setAggressive(false)
        banditEntity.getNavigation().stop()
    }

    override fun requiresUpdateEveryTick(): Boolean = true

    override fun tick() {
        val target = banditEntity.target ?: return
        banditEntity.getLookControl().setLookAt(target, 30.0f, 30.0f)

        ticksUntilNextPathRecalculation = max(ticksUntilNextPathRecalculation - 1, 0)
        if (ticksUntilNextPathRecalculation <= 0
            && (pathedTargetX == 0.0 && pathedTargetY == 0.0 && pathedTargetZ == 0.0
                    || target.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1.0
                    || banditEntity.getRandom().nextFloat() < 0.05f)
        ) {
            pathedTargetX = target.x
            pathedTargetY = target.y
            pathedTargetZ = target.z

            ticksUntilNextPathRecalculation = 4 + banditEntity.getRandom().nextInt(7)
            val targetDistanceSqr = banditEntity.distanceToSqr(target)
            if (targetDistanceSqr > 1024.0) ticksUntilNextPathRecalculation += 10
            else if (targetDistanceSqr > 256.0) ticksUntilNextPathRecalculation += 5

            if (!banditEntity.getNavigation().moveTo(target, speedModifier)) ticksUntilNextPathRecalculation += 15

            ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation)
        }

        ticksUntilNextAttack = max(ticksUntilNextAttack - 1, 0)
        checkAndPerformAttack(target)
    }

    protected open fun checkAndPerformAttack(target: LivingEntity) {
        if (!canPerformAttack(target)) return
        resetAttackCooldown()
        banditEntity.swing(InteractionHand.MAIN_HAND)
        banditEntity.doHurtTarget(getServerLevel(banditEntity), target)
    }

    protected fun resetAttackCooldown() {
        ticksUntilNextAttack = adjustedTickDelay(attackInterval)
    }

    fun isTimeToAttack(): Boolean = ticksUntilNextAttack <= 0

    protected fun canPerformAttack(target: LivingEntity): Boolean {
        return isTimeToAttack() && banditEntity.isWithinMeleeAttackRange(target) && banditEntity.sensing.hasLineOfSight(target)
    }
}
