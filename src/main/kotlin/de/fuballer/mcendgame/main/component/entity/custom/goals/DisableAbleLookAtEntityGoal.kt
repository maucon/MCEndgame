package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal

class DisableAbleLookAtEntityGoal(
    entity: Mob,
    type: Class<out LivingEntity>,
    range: Float,
) : LookAtPlayerGoal(entity, type, range) {
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