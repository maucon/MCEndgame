package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal

class DisableAbleWanderAroundFarGoal(
    pathAwareEntity: PathfinderMob,
    speed: Double,
    probability: Float = 0.001F,
) : WaterAvoidingRandomStrollGoal(pathAwareEntity, speed, probability) {
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