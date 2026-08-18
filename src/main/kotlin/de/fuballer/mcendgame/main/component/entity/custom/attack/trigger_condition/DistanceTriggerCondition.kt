package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DistanceTriggerCondition(
    private val minDistance: Double,
    private val maxDistance: Double,
    private val affectedByScale: Boolean = false,
) : TriggerCondition() {
    private val squaredMinDistance = minDistance * minDistance
    private val squaredMaxDistance = maxDistance * maxDistance

    constructor(maxDistance: Double, affectedByScale: Boolean = false) : this(0.0, maxDistance, affectedByScale = affectedByScale)

    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (target == null) return false
        val squaredDistance = attacker.distanceToSqr(target)
        if (!affectedByScale) return squaredDistance in squaredMinDistance..squaredMaxDistance

        val scale = attacker.scale
        val scaledMinDistance = minDistance * scale
        val scaledMaxDistance = maxDistance * scale
        return squaredDistance in (scaledMinDistance * scaledMinDistance)..(scaledMaxDistance * scaledMaxDistance)
    }
}