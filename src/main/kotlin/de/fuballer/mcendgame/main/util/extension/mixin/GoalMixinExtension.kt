package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.mixin.goal.MeleeAttackGoalAccessor
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal

object GoalMixinExtension {
    fun MeleeAttackGoal.setUpdateCountdownTicks(ticks: Int) = (this as MeleeAttackGoalAccessor).`mcendgame$setTicksUntilNextPathRecalculation`(ticks)
    fun MeleeAttackGoal.getUpdateCountdownTicks() = (this as MeleeAttackGoalAccessor).`mcendgame$getTicksUntilNextPathRecalculation`()

    fun MeleeAttackGoal.setTargetX(x: Double) = (this as MeleeAttackGoalAccessor).`mcendgame$setPathedTargetX`(x)
    fun MeleeAttackGoal.getTargetX() = (this as MeleeAttackGoalAccessor).`mcendgame$getPathedTargetX`()
    fun MeleeAttackGoal.setTargetY(y: Double) = (this as MeleeAttackGoalAccessor).`mcendgame$setPathedTargetY`(y)
    fun MeleeAttackGoal.getTargetY() = (this as MeleeAttackGoalAccessor).`mcendgame$getPathedTargetY`()
    fun MeleeAttackGoal.setTargetZ(z: Double) = (this as MeleeAttackGoalAccessor).`mcendgame$setPathedTargetZ`(z)
    fun MeleeAttackGoal.getTargetZ() = (this as MeleeAttackGoalAccessor).`mcendgame$getPathedTargetZ`()

    fun MeleeAttackGoal.setCooldown(cooldown: Int) = (this as MeleeAttackGoalAccessor).`mcendgame$setTicksUntilNextAttack`(cooldown)

}