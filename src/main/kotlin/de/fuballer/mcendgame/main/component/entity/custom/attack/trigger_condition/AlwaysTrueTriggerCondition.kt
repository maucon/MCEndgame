package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class AlwaysTrueTriggerCondition : TriggerCondition() {
    override fun doesTrigger(attacker: Mob, target: LivingEntity?) = true
}