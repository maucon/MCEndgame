package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class HorizontalDistanceTriggerCondition(
    private val minHorizontalDistance: Double,
    private val maxHorizontalDistance: Double,
    private val squaredMinHorizontalDistance: Double = minHorizontalDistance * minHorizontalDistance,
    private val squaredMaxHorizontalDistance: Double = maxHorizontalDistance * maxHorizontalDistance,
) : TriggerCondition() {
    constructor(maxHorizontalDistance: Double) : this(0.0, maxHorizontalDistance)

    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (target == null) return false
        return target.position().subtract(attacker.position()).horizontalDistanceSqr() in squaredMinHorizontalDistance..squaredMaxHorizontalDistance
    }
}