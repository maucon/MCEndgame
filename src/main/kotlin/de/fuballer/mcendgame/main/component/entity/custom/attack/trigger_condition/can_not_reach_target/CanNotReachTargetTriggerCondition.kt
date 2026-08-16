package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.can_not_reach_target

import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class CanNotReachTargetTriggerCondition(
    maxPathEndDistanceToTarget: Double,
    private val reach: Int = 1,
) : TriggerCondition() {
    private val maxDistanceSqr = maxPathEndDistanceToTarget * maxPathEndDistanceToTarget

    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        if (target == null) return false

        val navigation = (attacker as? CanNotReachTargetTriggerConditionInterface)?.getCanNotReachTargetTriggerConditionNavigation() ?: return false
        val path = navigation.createPath(target, reach) ?: return false

        val nodeCount = path.nodeCount
        if (nodeCount == 0) return attacker.distanceTo(target) > maxDistanceSqr
        val destinyPos = path.getEntityPosAtNode(attacker, nodeCount - 1)

        val sqrDistance = destinyPos.distanceToSqr(target.position())
        return sqrDistance > maxDistanceSqr
    }
}