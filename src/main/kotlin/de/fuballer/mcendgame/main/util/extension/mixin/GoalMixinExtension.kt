package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.mixin.goal.MeleeAttackGoalAccessor
import net.minecraft.entity.ai.goal.MeleeAttackGoal

object GoalMixinExtension {
    fun MeleeAttackGoal.setUpdateCountdownTicks(ticks: Int) = (this as MeleeAttackGoalAccessor).`mcendgame$setUpdateCountdownTicks`(ticks)
    fun MeleeAttackGoal.getUpdateCountdownTicks() = (this as MeleeAttackGoalAccessor).`mcendgame$getUpdateCountdownTicks`()

    fun MeleeAttackGoal.setTargetX(x: Double) = (this as MeleeAttackGoalAccessor).`mcendgame$setTargetX`(x)
    fun MeleeAttackGoal.getTargetX() = (this as MeleeAttackGoalAccessor).`mcendgame$getTargetX`()
    fun MeleeAttackGoal.setTargetY(y: Double) = (this as MeleeAttackGoalAccessor).`mcendgame$setTargetY`(y)
    fun MeleeAttackGoal.getTargetY() = (this as MeleeAttackGoalAccessor).`mcendgame$getTargetY`()
    fun MeleeAttackGoal.setTargetZ(z: Double) = (this as MeleeAttackGoalAccessor).`mcendgame$setTargetZ`(z)
    fun MeleeAttackGoal.getTargetZ() = (this as MeleeAttackGoalAccessor).`mcendgame$getTargetZ`()

    fun MeleeAttackGoal.setCooldown(cooldown: Int) = (this as MeleeAttackGoalAccessor).`mcendgame$setCooldown`(cooldown)

}