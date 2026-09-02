package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.goals

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditType
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isFacingTowards
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.pathfinder.Path
import java.util.*
import kotlin.math.*

open class BanditMeleeGoal(
    private val banditEntity: BanditEntity,
    private val speedModifier: Double,
) : Goal() {
    private var path: Path? = null
    private var pathedTargetX = 0.0
    private var pathedTargetY = 0.0
    private var pathedTargetZ = 0.0
    private var ticksUntilNextPathRecalculation = 0
    private var lastCanUseCheck: Long = 0

    private var ticksUntilNextAttack: Int = 0

    private val travelJumpMinNoElevationNodes: Int = 5

    private var combatMode = CombatMode.MOVE

    private var strafingForwardsTime = 0
    private var strafeForwards = 0.5F
    private var strafingSideTime = 0
    private var strafeSide = 0.5F

    private var blockingTicks = -1
    private var blockingDuration = 0

    private var shieldHit = false

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
        banditEntity.isSprinting = true
        ticksUntilNextPathRecalculation = 0
        ticksUntilNextAttack = 0
    }

    override fun stop() {
        banditEntity.target = null

        banditEntity.isSprinting = false
        banditEntity.setAggressive(false)
        banditEntity.getNavigation().stop()
        banditEntity.getBanditMoveControl().strafe(0F, 0F)
    }

    override fun requiresUpdateEveryTick(): Boolean = true

    override fun tick() {
        val target = banditEntity.target ?: return
        banditEntity.getLookControl().setLookAt(target, 30.0f, 30.0f)

        ticksUntilNextAttack = max(ticksUntilNextAttack - 1, 0)
        tickCombatModes(target)
    }

    private fun tickCombatModes(
        target: LivingEntity,
    ) {
        val attackRange = getAttackRange()
        val banditType = banditEntity.getBanditType()

        when (combatMode) {
            CombatMode.MOVE -> {
                tickPath(target)
                if (banditType.jumpWhileTravel) tryTravelJump()

                if (isDistanceToTargetGreaterThan(attackRange * 2)) return
                enterDuel(banditType)
            }

            CombatMode.DUEL -> {
                tickBlocking()

                val jumpCrit = banditType.jumpCritAttack
                tickDuelStrafe(banditType, target, attackRange, jumpCrit)
                if (!isBlocking()) checkAndPerformMeleeAttack(banditType, target, jumpCrit)

                if (!isDistanceToTargetGreaterThan(attackRange * 2.5)) return
                exitDuel(target)
            }
        }
    }

    private fun enterDuel(
        banditType: BanditType,
    ) {
        if (banditType.blockOnEnterDuel) startBlocking(banditType)

        banditEntity.navigation.stop()
        banditEntity.isSprinting = false
        strafeForwards = 0.5F
        strafingForwardsTime = 0
        strafingSideTime = 0
        combatMode = CombatMode.DUEL
    }

    private fun exitDuel(
        target: LivingEntity,
    ) {
        if (isBlocking()) stopBlocking()
        combatMode = CombatMode.MOVE
        banditEntity.isSprinting = true
        updatePath(target)
    }

    private fun tickBlocking() {
        if (shieldHit) stopBlocking()
        if (!isBlocking()) return
        if (++blockingTicks >= blockingDuration) stopBlocking()
    }

    private fun tickDuelStrafe(
        banditType: BanditType,
        target: LivingEntity,
        attackRange: Double,
        jumpCrit: Boolean,
    ) {
        if (jumpCrit && ticksUntilNextAttack < 10 && banditEntity.onGround() && !isBlocking()) {
            strafeForwards = 0.5F
            strafingForwardsTime = 10
            val moveControl = banditEntity.getBanditMoveControl()
            moveControl.strafe(strafeForwards, strafeSide)
            moveControl.jump()
            return
        }

        strafeForwards = getDuelStrafeForwards(target, attackRange)

        strafingSideTime++
        if (strafingSideTime >= banditType.sideStrafeUpdateTime) {
            strafingSideTime = 0
            if (banditEntity.random.nextFloat() < 0.3) strafeSide *= -1F
        }

        banditEntity.getBanditMoveControl().strafe(strafeForwards, strafeSide)
    }

    private fun getDuelStrafeForwards(
        target: LivingEntity,
        attackRange: Double,
    ): Float {
        val distanceSqr = banditEntity.distanceToSqr(target)
        if (distanceSqr > (attackRange * 2).pow(2)) {
            strafingForwardsTime = 0
            return 0.5F
        }

        if (distanceSqr < (attackRange * 0.5).pow(2)) {
            strafingForwardsTime = 0
            return -0.5F
        }

        val speed = getMovementSpeed() * banditEntity.getBanditMoveControl().speedModifier
        val distance = sqrt(distanceSqr)
        val distanceToAttackRange = distance - attackRange
        if (speed * ticksUntilNextAttack < distanceToAttackRange) {
            strafingForwardsTime = 0
            return 0.5F
        }

        strafingForwardsTime++
        if (strafingForwardsTime >= 20) {
            strafingForwardsTime = 0
            var changeDirectionProbability = 0.3
            val halfAttackRange = attackRange / 2
            if (strafeForwards > 0) changeDirectionProbability += 0.7 * (1 - (distance - halfAttackRange) / halfAttackRange).coerceAtLeast(0.0)
            if (banditEntity.random.nextFloat() < changeDirectionProbability) return strafeForwards * -1F
        }

        return strafeForwards
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

        val pathReachRange = max(getAttackRange().toInt() - 1, 1)
        val canMoveTo = banditEntity.navigation.moveTo(pathedTargetX, pathedTargetY, pathedTargetZ, pathReachRange, speedModifier)

        setTicksUntilNextPathRecalculation(target, canMoveTo)
    }

    private fun setTicksUntilNextPathRecalculation(
        target: LivingEntity,
        canMoveTo: Boolean,
    ) {
        ticksUntilNextPathRecalculation = 4 + banditEntity.getRandom().nextInt(7)
        val targetDistanceSqr = banditEntity.distanceToSqr(target)
        if (targetDistanceSqr > 1024.0) ticksUntilNextPathRecalculation += 10
        else if (targetDistanceSqr > 256.0) ticksUntilNextPathRecalculation += 5

        if (!canMoveTo) ticksUntilNextPathRecalculation += 15

        ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation)
    }

    private fun tryTravelJump() {
        if (!banditEntity.onGround()) return
        if (isElevationAhead()) return
        banditEntity.getBanditMoveControl().jump()
    }

    private fun isElevationAhead(): Boolean {
        val stepHeight = banditEntity.getAttributeValue(Attributes.STEP_HEIGHT)

        val path = banditEntity.navigation.path ?: return false
        if (path.isDone) return false
        val currentIndex = path.nextNodeIndex
        val start = currentIndex + 1
        val end = min(currentIndex + travelJumpMinNoElevationNodes, path.nodeCount - 1)

        var y = path.nextNode.y
        for (index in start..end) {
            val nextY = path.getNode(index).y
            if (nextY - y > stepHeight) return true
            y = nextY
        }

        return false
    }

    protected open fun checkAndPerformMeleeAttack(
        banditType: BanditType,
        target: LivingEntity,
        hasToBeCrit: Boolean = false,
    ) {
        if (!canPerformMeleeAttack(target)) return
        if (hasToBeCrit && !canCriticalAttack()) return

        banditEntity.swing(InteractionHand.MAIN_HAND)
        banditEntity.doHurtTarget(getServerLevel(banditEntity), target)
        setMeleeAttackCooldown()

        if (banditType.strafeBackAfterTargetHit) {
            strafeForwards = -0.5F
            strafingForwardsTime = 0
        }

        if (banditEntity.random.nextFloat() < banditType.blockAfterTargetHitProbability) startBlocking(banditType)
    }

    private fun canCriticalAttack() = banditEntity.fallDistance > 0.0
            && !banditEntity.onGround()
            && !banditEntity.onClimbable()
            && !banditEntity.isInWater
            && !banditEntity.isPassenger
            && !banditEntity.isSprinting

    protected fun canPerformMeleeAttack(target: LivingEntity): Boolean {
        if (ticksUntilNextAttack > 0) return false
        if (!banditEntity.isFacingTowards(target, 30.0)) return false
        if (!banditEntity.isWithinMeleeAttackRange(target)) return false
        if (!banditEntity.sensing.hasLineOfSight(target)) return false
        return true
    }

    protected fun setMeleeAttackCooldown() {
        val attackSpeed = banditEntity.getAttributeValue(Attributes.ATTACK_SPEED)
        val attackCooldown = ceil(20 / attackSpeed).toInt()
        ticksUntilNextAttack = adjustedTickDelay(attackCooldown)
    }

    private fun isDistanceToTargetGreaterThan(dist: Double): Boolean {
        val target = banditEntity.target ?: return false
        return banditEntity.distanceToSqr(target) > dist * dist
    }

    private fun getAttackRange() = banditEntity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE)

    private fun getMovementSpeed() = banditEntity.getAttributeValue(Attributes.MOVEMENT_SPEED)

    private fun hasShield() = banditEntity.offhandItem.`is`(CustomTags.SHIELD)

    private fun startBlocking(
        banditType: BanditType,
    ) {
        if (!hasShield()) return
        if (banditEntity.getCooldowns().isOnCooldown(banditEntity.offhandItem)) return

        banditEntity.startUsingItem(InteractionHand.OFF_HAND)
        blockingTicks = 0
        blockingDuration = banditType.blockDuration()
    }

    private fun stopBlocking() {
        banditEntity.stopUsingItem()
        blockingTicks = -1
        shieldHit = false
    }

    private fun isBlocking() = blockingTicks >= 0

    fun tookHit() {
        if (!isBlocking()) return
        shieldHit = true
    }

    private enum class CombatMode {
        MOVE,
        DUEL,
    }
}
