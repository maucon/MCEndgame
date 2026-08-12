package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class CanNotReachTargetTriggerCondition(
    maxPathEndDistanceToTarget: Double,
    private val reach: Int = 1,
) : TriggerCondition() {
    private val maxDistanceSqr = maxPathEndDistanceToTarget * maxPathEndDistanceToTarget

    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        if (target == null) return false

        val path = attacker.navigation.createPath(target, reach) ?: return true
        val endNode = path.endNode ?: return true

        val sqrDistance = endNode.distanceToSqr(target.blockPosition())
        return sqrDistance > maxDistanceSqr
    }
}