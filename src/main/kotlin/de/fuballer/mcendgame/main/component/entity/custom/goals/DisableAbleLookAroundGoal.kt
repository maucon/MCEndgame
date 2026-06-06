package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal

class DisableAbleLookAroundGoal(
    entity: Mob,
) : RandomLookAroundGoal(entity) {
    var isDisabled = false

    override fun canUse(): Boolean {
        if (isDisabled) return false
        return super.canUse()
    }

    override fun canContinueToUse(): Boolean {
        if (isDisabled) return false
        return super.canContinueToUse()
    }
}