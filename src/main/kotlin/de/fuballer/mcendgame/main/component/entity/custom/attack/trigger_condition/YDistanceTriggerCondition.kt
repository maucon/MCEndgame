package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class YDistanceTriggerCondition(
    private val minYOffset: Double,
    private val maxYOffset: Double,
    private val affectedByScale: Boolean = false,
) : TriggerCondition() {
    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (target == null) return false

        var min = minYOffset
        var max = maxYOffset
        if (affectedByScale) {
            val scale = attacker.scale
            min *= scale
            max *= scale
        }

        return (target.position().y - attacker.position().y) in min..max
    }
}