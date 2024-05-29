package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskTimer
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsForcedVehicle
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.max

private const val RANGE = 30.0
private const val MAX_TIME = 5000 // ms
private const val VELOCITY = 0.3

private const val PARTICLE_STEP_DISTANCE = 0.25
private const val PARTICLE_PER_STEP = 1

object PullAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val targets = DungeonUtil.getNearbyPlayers(caster, RANGE)

        for (target in targets) {
            if (target.isInsideVehicle) continue

            val armorStand = target.world.spawnEntity(target.location, EntityType.ARMOR_STAND) as ArmorStand
            armorStand.isVisible = false
            armorStand.isInvulnerable = true
            armorStand.isSmall = true
            armorStand.addPassenger(target)
            armorStand.setIsForcedVehicle()

            PullRunnable(caster, target, armorStand, System.currentTimeMillis()).runTaskTimer(0, 1)
        }
    }

    private class PullRunnable(
        private val caster: LivingEntity,
        private val target: LivingEntity,
        private val armorStand: ArmorStand,
        private val startTime: Long,
    ) : BukkitRunnable() {
        override fun run() {
            val distanceVector = caster.location.toVector().subtract(armorStand.location.toVector())
            val direction = distanceVector.clone().normalize()

            val yVelocity = if (armorStand.isOnGround) max(direction.y, 0.1 / VELOCITY) else direction.y
            val velocity = Vector(direction.x, yVelocity, direction.z).multiply(VELOCITY)
            armorStand.velocity = velocity

            drawLine()

            val distance = distanceVector.length()
            if (distance < 0.5 || System.currentTimeMillis() - startTime > MAX_TIME) {
                armorStand.remove()
                this.cancel()
            }
        }

        private fun drawLine() {
            val casterPos = caster.location.toVector().add(caster.eyeLocation.toVector()).multiply(0.5)
            val targetPos = target.location.toVector().add(target.eyeLocation.toVector()).multiply(0.5)

            val distanceVector = casterPos.subtract(targetPos)
            val distance = distanceVector.length()
            val stepVector = distanceVector.clone().normalize().multiply(PARTICLE_STEP_DISTANCE)

            val currentStep = targetPos.clone().add(stepVector)
            var currentLength = PARTICLE_STEP_DISTANCE
            while (currentLength <= distance) {
                caster.world.spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    currentStep.x, currentStep.y, currentStep.z,
                    PARTICLE_PER_STEP, 0.05, 0.05, 0.05, 0.0
                )

                currentStep.add(stepVector)
                currentLength += PARTICLE_STEP_DISTANCE
            }
        }
    }
}