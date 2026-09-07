package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.goals

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.item.BowItem
import java.util.*

class BanditBowGoal(
    private val banditEntity: BanditEntity,
    private val speedModifier: Double,
    attackRadius: Float,
) : Goal() {
    private val attackRadiusSqr: Float = attackRadius * attackRadius
    private var attackTime = -1
    private var seeTime = 0
    private var strafingClockwise = false
    private var strafingBackwards = false
    private var strafingTime = -1

    init {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK))
    }

    override fun canUse() = banditEntity.target != null && isHoldingBow()

    private fun isHoldingBow() = banditEntity.mainHandItem.`is`(CustomTags.BOW)

    override fun canContinueToUse() = canUse()

    override fun start() {
        super.start()
        banditEntity.setAggressive(true)
    }

    override fun stop() {
        super.stop()
        banditEntity.setAggressive(false)
        seeTime = 0
        attackTime = -1
        banditEntity.stopUsingItem()
    }

    override fun requiresUpdateEveryTick() = true

    override fun tick() {
        val banditType = banditEntity.getBanditType()
        val target = banditEntity.target ?: return

        val hasLineOfSight = banditEntity.sensing.hasLineOfSight(target)
        val hadLineOfSight = seeTime > 0
        if (hasLineOfSight != hadLineOfSight) seeTime = 0
        if (hasLineOfSight) seeTime++ else seeTime--

        val targetDistSqr = banditEntity.distanceToSqr(target)
        if (targetDistSqr <= attackRadiusSqr && seeTime >= 20) {
            banditEntity.getNavigation().stop()
            strafingTime++
        } else {
            banditEntity.getNavigation().moveTo(target, speedModifier)
            strafingTime = -1
        }

        if (strafingTime >= banditType.sideStrafeUpdateTime) {
            if (banditEntity.getRandom().nextFloat() < 0.3) strafingClockwise = !strafingClockwise
            if (banditEntity.getRandom().nextFloat() < 0.3) strafingBackwards = !strafingBackwards
            strafingTime = 0
        }

        if (strafingTime > -1) {
            if (targetDistSqr > attackRadiusSqr * 0.75f) {
                strafingBackwards = false
            } else if (targetDistSqr < attackRadiusSqr * 0.5f) {
                strafingBackwards = true
            }

            banditEntity.getBanditMoveControl().strafe(
                if (strafingBackwards) -0.5f else 0.5f,
                if (strafingClockwise) 0.5f else -0.5f,
            )

            banditEntity.lookAt(target, 30.0f, 30.0f)
        } else {
            banditEntity.getLookControl().setLookAt(target, 30.0f, 30.0f)
        }

        if (banditEntity.isUsingItem) {
            if (!hasLineOfSight && seeTime < -60) {
                banditEntity.stopUsingItem()
            } else if (hasLineOfSight) {
                val pullTime = banditEntity.ticksUsingItem
                if (pullTime >= 20) {
                    banditEntity.stopUsingItem()
                    banditEntity.performRangedAttack(target, BowItem.getPowerForTime(pullTime))
                    attackTime = 20 // TODO use bow pull ticks
                }
            }
        } else if (--attackTime <= 0 && seeTime >= -60) {
            banditEntity.startUsingItem(InteractionHand.MAIN_HAND)
        }
    }
}