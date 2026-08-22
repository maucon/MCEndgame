package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class CanMoveUpTriggerCondition(
    private val distance: Double,
) : TriggerCondition() {
    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        val box = attacker.boundingBox.expandTowards(0.0, distance, 0.0)
        return attacker.level().noBlockCollision(attacker, box)
    }
}