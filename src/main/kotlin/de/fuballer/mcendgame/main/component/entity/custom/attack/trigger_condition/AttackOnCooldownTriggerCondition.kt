package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class AttackOnCooldownTriggerCondition(
    private val attack: Attack<*>,
    private val minCooldown: Int,
) : TriggerCondition() {
    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        val customAttacksMob = attacker as? CustomAttacksMob<*> ?: return false
        val cooldown = customAttacksMob.attackCooldowns[attack] ?: return false
        return cooldown >= minCooldown
    }
}