package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

private fun getSnowballAmount(healthPercent: Double) = 3 + (25 * (1 - healthPercent)).toInt()

object ShootSnowballsAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return
        val target = creature.target ?: return

        val maxHealth = caster.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: caster.health
        val healthPercent = caster.health / maxHealth

        val snowballAmount = getSnowballAmount(healthPercent)
        for (i in 0 until snowballAmount) {
            ShootSnowballRunnable(caster, target, healthPercent, true).runTaskLater(i.toLong())
            ShootSnowballRunnable(caster, target, healthPercent, false).runTaskLater(i.toLong())
        }
    }

    private class ShootSnowballRunnable(
        private val caster: Creature,
        private val target: LivingEntity,
        private val healthPercent: Double,
        private val side: Boolean,
    ) : BukkitRunnable() {
        override fun run() {
            val targetPos = target.eyeLocation.toVector()
            val casterLoc = caster.location.clone().add(0.0, 1.1 + 0.5 * (1 - healthPercent), 0.0)

            val distanceVector = targetPos.subtract(casterLoc.toVector())
            val direction = distanceVector.clone().normalize()

            val offsetRotation = if (side) 90.0 else -90.0
            val offsetDistance = 0.2 + 0.22 * (1 - healthPercent)
            val startOffset = direction.clone()
                .rotateAroundY(Math.toRadians(offsetRotation))
                .multiply(offsetDistance)
            val startLoc = casterLoc.clone().add(startOffset).add(direction.clone().multiply(0.2))

            val snowball = caster.world.spawnEntity(startLoc, EntityType.SNOWBALL)

            snowball.velocity = direction

            val distance = distanceVector.multiply(Vector(1.0, 0.0, 1.0)).length()
            snowball.velocity = snowball.velocity.add(Vector(0.0, 0.012 * distance, 0.0))

            snowball.velocity = snowball.velocity.multiply(getRandomSpread())
        }

        private fun getRandomSpread(): Vector {
            val maxRandom = 0.05 + 0.15 * (1 - healthPercent)
            val x = 1 + Math.random() * maxRandom * 2 - maxRandom
            val y = 1 + Math.random() * maxRandom * 2 - maxRandom
            val z = 1 + Math.random() * maxRandom * 2 - maxRandom
            return Vector(x, y, z)
        }
    }
}