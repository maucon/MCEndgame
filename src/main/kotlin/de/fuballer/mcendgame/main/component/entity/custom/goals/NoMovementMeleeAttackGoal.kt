package de.fuballer.mcendgame.main.component.entity.custom.goals

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.MeleeAttackMob
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class NoMovementMeleeAttackGoal<T>(
    private val entity: T,
    private val intervalTicks: Int,
    range: Double,
    private val initialCooldown: Int = intervalTicks,
) : DisableAbleGoal() where T : Mob, T : MeleeAttackMob {
    private var squaredRange = range * range
    private var cooldown = 0

    override fun canUse(): Boolean {
        if (isDisabled) return false
        val target: LivingEntity = entity.target ?: return false
        return target.isAlive
    }

    override fun start() {
        cooldown = adjustedTickDelay(initialCooldown)
    }

    override fun canContinueToUse() = canUse()

    override fun stop() {}

    override fun tick() {
        if (--cooldown > 0) return
        cooldown = 0

        val target = entity.target ?: return
        val squaredDistanceToTarget = entity.distanceToSqr(target)
        if (squaredDistanceToTarget > squaredRange) return
        if (!entity.sensing.hasLineOfSight(target)) return

        entity.meleeAttack(target)
        cooldown = adjustedTickDelay(intervalTicks)
    }

    fun setRange(range: Double) {
        squaredRange = range * range
    }
}