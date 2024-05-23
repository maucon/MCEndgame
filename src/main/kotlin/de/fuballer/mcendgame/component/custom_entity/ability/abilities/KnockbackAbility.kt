package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.util.DungeonUtil
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.math.min

private val REMOVE_Y_VEC = Vector(1.0, 0.0, 1.0)
private val Y_KNOCKBACK_VEC = Vector(0.0, 0.3, 0.0)

private const val KNOCKBACK_RANGE = 10.0
private const val MAX_KNOCKBACK_STRENGTH = 2.0
fun getKnockbackStrength(distance: Double) = MAX_KNOCKBACK_STRENGTH * (1 - min(1.0, distance / KNOCKBACK_RANGE))

object KnockbackAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val location = caster.eyeLocation
        caster.world.spawnParticle(
            Particle.CRIT,
            location.x, location.y, location.z,
            100, 0.0, 0.0, 0.0, 3.0
        )

        val castLocVec = caster.location.toVector()

        val targets = DungeonUtil.getNearbyPlayers(caster, KNOCKBACK_RANGE)
        for (player in targets) {
            val distanceVector = player.location.toVector().subtract(castLocVec)
            val distance = distanceVector.length()
            val direction = distanceVector.multiply(REMOVE_Y_VEC).normalize().add(Y_KNOCKBACK_VEC)
            val knockbackStrength = getKnockbackStrength(distance)
            val knockback = direction.multiply(knockbackStrength)

            player.velocity = knockback
        }
    }
}