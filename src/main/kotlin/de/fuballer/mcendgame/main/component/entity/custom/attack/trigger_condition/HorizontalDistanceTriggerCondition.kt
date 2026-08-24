package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import kotlin.math.pow

class HorizontalDistanceTriggerCondition(
    private val minHorizontalDistance: Double,
    private val maxHorizontalDistance: Double,
    private val affectedByScale: Boolean = false
) : TriggerCondition() {
    constructor(maxHorizontalDistance: Double, affectedByScale: Boolean = false) : this(0.0, maxHorizontalDistance, affectedByScale)

    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (target == null) return false

        val scale = if (affectedByScale) attacker.scale else 1F
        val minSqr = (minHorizontalDistance * scale).pow(2)
        val maxSqr = (maxHorizontalDistance * scale).pow(2)
        return target.position().subtract(attacker.position()).horizontalDistanceSqr() in minSqr..maxSqr
    }
}