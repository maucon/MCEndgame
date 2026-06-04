package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DistanceTriggerCondition(
    private val minDistance: Double,
    private val maxDistance: Double,
    private val squaredMinDistance: Double = minDistance * minDistance,
    private val squaredMaxDistance: Double = maxDistance * maxDistance,
) : TriggerCondition() {
    constructor(maxDistance: Double) : this(0.0, maxDistance)

    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (target == null) return false
        return attacker.distanceToSqr(target) in squaredMinDistance..squaredMaxDistance
    }
}