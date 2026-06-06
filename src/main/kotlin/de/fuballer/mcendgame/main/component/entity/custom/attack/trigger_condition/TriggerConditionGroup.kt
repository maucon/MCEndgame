package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class TriggerConditionGroup(
    private val joinType: TriggerConditionJoinType,
    private val trigger: List<TriggerCondition>,
) : TriggerCondition() {
    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ) = when (joinType) {
        TriggerConditionJoinType.OR -> trigger.any { it.doesTrigger(attacker, target) }
        TriggerConditionJoinType.AND -> trigger.all { it.doesTrigger(attacker, target) }
    }

    enum class TriggerConditionJoinType {
        OR,
        AND,
    }
}