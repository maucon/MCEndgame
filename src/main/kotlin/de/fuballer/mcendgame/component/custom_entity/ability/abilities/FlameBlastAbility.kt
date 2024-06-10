package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

private const val STEP_DELAY = 2
private const val RADIUS_PER_STEP = 0.3
private const val RADIUS_INCREASE_STEPS = 15
private const val FULL_RADIUS_STEPS = 10
private const val FULL_RADIUS = RADIUS_INCREASE_STEPS * RADIUS_PER_STEP

private const val FIRE_TICKS = 80
private const val BASE_DAMAGE = 20.0
private const val DAMAGE_PER_TIER = 3.5

private const val PARTICLE_LOCATIONS_PER_RING = 10
private const val PARTICLE_LOCATION_RADIUS_MULTIPLIER = 1

object FlameBlastAbility : Ability {
    override fun canCast(caster: LivingEntity): Boolean {
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)
        return targets.isNotEmpty()
    }

    override fun cast(caster: LivingEntity) {
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)

        for (target in targets) {
            castOnTarget(caster, target)
        }
    }

    private fun castOnTarget(caster: LivingEntity, target: LivingEntity) {
        for (i in 1..RADIUS_INCREASE_STEPS) {
            CastFlameBlastBuildUpRunnable(target.location, i * RADIUS_PER_STEP, i % 5 == 0)
                .runTaskLater((i * STEP_DELAY).toLong())
        }
        for (i in 1..FULL_RADIUS_STEPS) {
            CastFlameBlastBuildUpRunnable(target.location, FULL_RADIUS, i % 5 == 0)
                .runTaskLater(((i + RADIUS_INCREASE_STEPS) * STEP_DELAY).toLong())
        }

        val damage = BASE_DAMAGE + DAMAGE_PER_TIER * (caster.getMapTier() ?: 0)
        CastFlameBlastDamageRunnable(caster, damage, target.location)
            .runTaskLater(((FULL_RADIUS_STEPS + RADIUS_INCREASE_STEPS) * STEP_DELAY).toLong())
    }

    private class CastFlameBlastBuildUpRunnable(
        private val location: Location,
        private val radius: Double,
        private val sound: Boolean
    ) : BukkitRunnable() {
        override fun run() {
            val world = location.world ?: return

            val locations = max(1, (PARTICLE_LOCATIONS_PER_RING * (radius * PARTICLE_LOCATION_RADIUS_MULTIPLIER)).toInt())
            val rotationRadians = 360 / locations * PI / 180

            val offset = Vector(radius, 0.0, 0.0)
            for (i in 0 until locations) {
                val calcLocation = location.clone().add(offset)

                world.spawnParticle(
                    Particle.SMOKE_NORMAL,
                    calcLocation.x, calcLocation.y, calcLocation.z,
                    2, 0.2, 0.1, 0.2, 0.01
                )

                offset.rotateAroundY(rotationRadians)
            }

            if (sound)
                world.playSound(location, Sound.BLOCK_FIRE_AMBIENT, SoundCategory.AMBIENT, 0.7f, 1f)

            this.cancel()
        }

    }

    private class CastFlameBlastDamageRunnable(
        private val caster: LivingEntity,
        private val damage: Double,
        private val location: Location
    ) : BukkitRunnable() {
        override fun run() {
            val world = location.world ?: return

            world.spawnParticle(
                Particle.LAVA,
                location.x, location.y, location.z,
                100, FULL_RADIUS / 2, 0.1, FULL_RADIUS / 2, 0.01
            )

            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.7f, 1f)

            dealDamage()

            this.cancel()
        }

        private fun dealDamage() {
            val world = location.world ?: return
            for (player in world.players) {
                val pLoc = player.location
                if (sqrt((location.x - pLoc.x).pow(2) + (location.z - pLoc.z).pow(2)) > FULL_RADIUS) continue
                if (location.y - pLoc.y > 2 || location.y - pLoc.y < -2) continue

                player.damage(damage, caster)
                player.fireTicks = FIRE_TICKS
            }
        }
    }
}