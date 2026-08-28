package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.goals

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.pathfinder.Path
import java.util.*
import kotlin.math.ceil
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
    private var lastCanUseCheck: Long = 0

    init {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP))
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
        banditEntity.isSprinting = true
        ticksUntilNextPathRecalculation = 0
        ticksUntilNextAttack = 0
    }

    override fun stop() {
        banditEntity.target = null

        banditEntity.isSprinting = false
        banditEntity.setAggressive(false)
        banditEntity.getNavigation().stop()
    }

    override fun requiresUpdateEveryTick(): Boolean = true

    override fun tick() {
        val target = banditEntity.target ?: return
        banditEntity.getLookControl().setLookAt(target, 30.0f, 30.0f)

        tickPath(target)

        if (shouldJump()) banditEntity.jumpControl.jump()

        ticksUntilNextAttack = max(ticksUntilNextAttack - 1, 0)
        checkAndPerformMeleeAttack(target)
    }

    private fun tickPath(target: LivingEntity) {
        ticksUntilNextPathRecalculation = max(ticksUntilNextPathRecalculation - 1, 0)
        if (ticksUntilNextPathRecalculation > 0) return

        if (pathedTargetX == 0.0 && pathedTargetY == 0.0 && pathedTargetZ == 0.0) updatePath(target)
        else if (target.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1.0) updatePath(target)
        else if (banditEntity.getRandom().nextFloat() < 0.05f) updatePath(target)
    }

    private fun updatePath(target: LivingEntity) {
        pathedTargetX = target.x
        pathedTargetY = target.y
        pathedTargetZ = target.z

        setTicksUntilNextPathRecalculation(target)
    }

    private fun setTicksUntilNextPathRecalculation(target: LivingEntity) {
        ticksUntilNextPathRecalculation = 4 + banditEntity.getRandom().nextInt(7)
        val targetDistanceSqr = banditEntity.distanceToSqr(target)
        if (targetDistanceSqr > 1024.0) ticksUntilNextPathRecalculation += 10
        else if (targetDistanceSqr > 256.0) ticksUntilNextPathRecalculation += 5

        if (!banditEntity.getNavigation().moveTo(target, speedModifier)) ticksUntilNextPathRecalculation += 15

        ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation)
    }

    protected open fun checkAndPerformMeleeAttack(target: LivingEntity) {
        if (!canPerformMeleeAttack(target)) return
        resetMeleeAttackCooldown()
        banditEntity.swing(InteractionHand.MAIN_HAND)
        banditEntity.doHurtTarget(getServerLevel(banditEntity), target)
    }

    protected fun resetMeleeAttackCooldown() {
        val attackSpeed = banditEntity.getAttributeValue(Attributes.ATTACK_SPEED)
        val attackCooldown = ceil(20 / attackSpeed).toInt()
        ticksUntilNextAttack = adjustedTickDelay(attackCooldown)
    }

    protected fun canPerformMeleeAttack(target: LivingEntity): Boolean {
        return ticksUntilNextAttack <= 0 && banditEntity.isWithinMeleeAttackRange(target) && banditEntity.sensing.hasLineOfSight(target)
    }

    private fun shouldJump(): Boolean {
        return false
    }
}
