package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class YDistanceTriggerCondition(
    private val minYOffset: Double,
    private val maxYOffset: Double,
) : TriggerCondition() {
    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (target == null) return false
        return (target.position().y - attacker.position().y) in minYOffset..maxYOffset
    }
}