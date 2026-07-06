package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity

class YDistanceTriggerCondition(
    private val minYOffset: Double,
    private val maxYOffset: Double,
) : TriggerCondition() {
    override fun doesTrigger(
        attacker: MobEntity,
        target: LivingEntity?,
    ): Boolean {
        if (target == null) return false
        return (target.pos.y - attacker.pos.y) in minYOffset..maxYOffset
    }
}