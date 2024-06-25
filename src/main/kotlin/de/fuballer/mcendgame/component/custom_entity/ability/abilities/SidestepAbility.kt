package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.util.LocationUtil
import org.bukkit.Particle
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import kotlin.random.Random

private const val MAX_RANGE_TO_TARGET = 8.0
private const val MIN_ANGLE = 75.0
private const val MAX_ANGLE = 135.0

object SidestepAbility : Ability {
    override fun canCast(caster: LivingEntity): Boolean {
        val creature = caster as? Creature ?: return false
        val target = creature.target ?: return false

        val distance = creature.location.toVector().subtract(target.location.toVector()).length()

        return distance <= MAX_RANGE_TO_TARGET
    }

    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return
        val target = creature.target ?: return

        val casterLocation = caster.location.clone()
        val direction = if (Random.nextBoolean()) 1 else -1

        var maxValidAngle = 0.0
        while (maxValidAngle + 1 <= MAX_ANGLE) {
            val rotatedLocation = LocationUtil.getLocationRotatedAroundLocationsYAxis(casterLocation, target.location, (maxValidAngle + 1) * direction)

            if (LocationUtil.isInsideUnpassableBlock(rotatedLocation, caster.boundingBox)) break
            maxValidAngle++
        }

        if (maxValidAngle < MIN_ANGLE) return
        val finalAngle = MIN_ANGLE + (maxValidAngle - MIN_ANGLE) * Random.nextDouble()

        val finalLocation = LocationUtil.getLocationRotatedAroundLocationsYAxis(casterLocation, target.location, finalAngle * direction)
        caster.teleport(finalLocation)

        caster.world.spawnParticle(Particle.SMOKE, casterLocation.x, casterLocation.y + 1, casterLocation.z, 15, 0.2, 0.5, 0.2, 0.01)
        caster.world.spawnParticle(Particle.SMOKE, finalLocation.x, finalLocation.y + 1, finalLocation.z, 50, 0.2, 0.5, 0.2, 0.05)
    }
}