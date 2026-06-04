package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

abstract class TriggerCondition {
    abstract fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean
}