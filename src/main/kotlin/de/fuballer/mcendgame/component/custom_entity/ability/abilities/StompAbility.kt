package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskTimer
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

private fun getKnockbackStrength(
    distance: Double,
) = MAX_STOMP_KNOCKBACK * (1 - min(1.0, distance / STOMP_RADIUS))

private const val STOMP_RADIUS = 8.0
private const val MAX_STOMP_KNOCKBACK = 1.2

private val REMOVE_Y_VEC = Vector(1.0, 0.0, 1.0)
private val Y_KNOCKBACK_VEC = Vector(0.0, 0.6, 0.0)

object StompAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return
        val target = creature.target ?: return

        caster.velocity = getLeapVector(caster, target)

        StompImpactRunnable(creature, System.currentTimeMillis()).runTaskTimer(10, 2)
    }

    private fun getLeapVector(caster: LivingEntity, target: LivingEntity): Vector {
        val yVelocity = getLeapYVelocity(caster, target)

        val targetOffset = target.location.y - caster.location.y
        val leapDuration = getLeapDuration(yVelocity, targetOffset)

        val horizontalVelocity = getLeapHorizontalVelocity(caster, target, leapDuration)

        val leapVector = target.location.toVector().subtract(caster.location.toVector())
        leapVector.y = 0.0
        leapVector.normalize()
        leapVector.multiply(horizontalVelocity)

        leapVector.y = yVelocity

        return leapVector
    }

    private fun getLeapYVelocity(caster: LivingEntity, target: LivingEntity): Double {
        val leapHighestPoint = max(0.0, target.location.y - caster.location.y) + 5
        return 0.4 * sqrt(leapHighestPoint)
    }

    private fun getLeapHorizontalVelocity(caster: LivingEntity, target: LivingEntity, ticks: Int): Double {
        val horizontalDistance = sqrt((caster.location.x - target.location.x).pow(2) + (caster.location.z - target.location.z).pow(2))
        val horizontalVelocity = (horizontalDistance * (1 - 0.91)) / (1 - 0.91.pow(ticks + 1)) // 0.91 minecraft horizontal air resistance
        return horizontalVelocity * 1.4 // FIXME dont know why its too little velocity doesnt make any sense
    }

    private fun getLeapDuration(yVelocity: Double, yOffset: Double): Int {
        var currentY = 0.0
        var currentVelocity = yVelocity
        var ticks = 0

        while (currentVelocity > 0 || currentY > yOffset) {
            currentVelocity -= 0.08 // minecraft gravity
            currentVelocity *= 0.98 // minecraft vertical air resistance
            currentY += currentVelocity

            ticks++
        }

        return ticks
    }

    private class StompImpactRunnable(
        private val caster: LivingEntity,
        private val startTime: Long
    ) : BukkitRunnable() {
        override fun run() {
            if (!keepRunning()) {
                this.cancel()
                return
            }
            if (!caster.isOnGround) return

            val maxHealth = caster.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: caster.health
            val healthPercent = caster.health / maxHealth

            val castLocVec = caster.location.toVector()
            val targets = DungeonUtil.getNearbyPlayers(caster, STOMP_RADIUS)

            for (player in targets) {
                val distanceVector = player.location.toVector().subtract(castLocVec)
                val distance = distanceVector.length()
                val direction = distanceVector.multiply(REMOVE_Y_VEC).normalize().add(Y_KNOCKBACK_VEC)
                val knockbackStrength = getKnockbackStrength(distance)
                val knockback = direction.multiply(knockbackStrength)

                player.velocity = knockback
            }

            createParticle()
            caster.world.playSound(caster.location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, (3 - 2.5 * healthPercent).toFloat(), 0.8f)

            this.cancel()
        }

        private fun keepRunning(): Boolean {
            if (caster.isDead) return false
            if (System.currentTimeMillis() - startTime > 5000) return false
            return true
        }

        private fun createParticle() {
            val world = caster.world
            val location = caster.location
            world.spawnParticle(
                Particle.CLOUD,
                location.x, location.y + 0.05, location.z,
                100, 3.0, 0.1, 3.0, 0.15
            )
        }
    }
}