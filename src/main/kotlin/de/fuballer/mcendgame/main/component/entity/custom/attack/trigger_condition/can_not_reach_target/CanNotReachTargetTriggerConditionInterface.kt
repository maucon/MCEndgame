package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.can_not_reach_target

import net.minecraft.world.entity.ai.navigation.PathNavigation

interface CanNotReachTargetTriggerConditionInterface {
    fun getCanNotReachTargetTriggerConditionNavigation(): PathNavigation
}