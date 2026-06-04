package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.world.entity.ai.goal.Goal

open class DisableAbleGoal : Goal() {
    var isDisabled = false
    override fun canUse() = !isDisabled
    override fun canContinueToUse() = !isDisabled
}