package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class HealthTriggerCondition(
    private val minHealthPercent: Double,
    private val maxHealthPercent: Double,
) : TriggerCondition() {
    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        val percent = (attacker.health / attacker.maxHealth).toDouble()
        return percent in minHealthPercent..maxHealthPercent
    }
}