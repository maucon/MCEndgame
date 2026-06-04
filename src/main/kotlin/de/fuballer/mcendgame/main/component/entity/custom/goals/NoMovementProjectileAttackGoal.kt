package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.monster.RangedAttackMob
import kotlin.math.sqrt

class NoMovementProjectileAttackGoal<T>(
    private val entity: T,
    private val intervalTicks: Int,
    private val maxShootRange: Float,
    private val initialCooldown: Int = intervalTicks,
) : DisableAbleGoal() where T : Mob, T : RangedAttackMob {
    private var targetSeenTicks = 0
    private var cooldown = 0
    private val maxShootRangeSquared = maxShootRange * maxShootRange

    override fun canUse(): Boolean {
        if (isDisabled) return false
        val target: LivingEntity = entity.target ?: return false
        return target.isAlive
    }

    override fun start() {
        cooldown = adjustedTickDelay(initialCooldown)
    }

    override fun canContinueToUse() = canUse()

    override fun stop() {
        targetSeenTicks = 0
    }

    override fun tick() {
        val target = entity.target ?: return

        val canSeeTarget = entity.sensing.hasLineOfSight(target)
        targetSeenTicks = if (canSeeTarget) targetSeenTicks + 1 else 0

        val squaredDistanceToTarget = entity.distanceToSqr(target)
        if (squaredDistanceToTarget > maxShootRangeSquared) return

        if (--cooldown > 0) return
        cooldown = adjustedTickDelay(intervalTicks)

        if (!canSeeTarget) return

        var rangePercentage = sqrt(squaredDistanceToTarget).toFloat() / maxShootRange
        rangePercentage = Mth.clamp(rangePercentage, 0.1f, 1.0f)
        entity.performRangedAttack(target, rangePercentage)
    }
}