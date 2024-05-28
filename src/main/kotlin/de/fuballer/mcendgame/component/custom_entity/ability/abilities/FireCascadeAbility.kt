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
import kotlin.math.pow
import kotlin.math.sqrt

private const val FIRE_CASCADE_DISTANCE = 1
private const val FIRE_CASCADE_STEPS_AFTER_PLAYER = 15
private const val FIRE_CASCADE_ACTIVATION_DELAY = 10L // in ticks

private const val FIRE_CASCADE_STEP_DELAY = 0.7 // in ticks

private const val FIRE_CASCADE_STEPS_PER_SOUND = 5
private const val FIRE_CASCADE_DAMAGE = 5.0
private const val FIRE_CASCADE_DAMAGE_PER_LEVEL = 3.0
private const val FIRE_CASCADE_FIRE_TICKS = 100

object FireCascadeAbility : Ability {
    override fun canCast(caster: LivingEntity): Boolean {
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)
        return targets.isNotEmpty()
    }

    override fun cast(caster: LivingEntity) {
        val mapTier = caster.getMapTier() ?: 1
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)

        for (target in targets) {
            castOnTarget(mapTier, caster, target)
        }
    }

    private fun castOnTarget(mapTier: Int, caster: LivingEntity, target: LivingEntity) {
        val vector = target.location.subtract(caster.location).toVector()
        val amount = vector.length().toInt() / FIRE_CASCADE_DISTANCE + FIRE_CASCADE_STEPS_AFTER_PLAYER
        val addVector = vector.normalize().multiply(FIRE_CASCADE_DISTANCE)
        val offsetVector = addVector.clone()

        var i = 1
        while (i < amount) {
            val stepDelay = (i * FIRE_CASCADE_STEP_DELAY).toLong()
            val sound = (i - 1) % FIRE_CASCADE_STEPS_PER_SOUND == 0

            CastFireCascadeRunnable(caster, 0.0, caster.location.add(offsetVector), true, sound)
                .runTaskLater(stepDelay)

            CastFireCascadeRunnable(
                caster,
                FIRE_CASCADE_DAMAGE + mapTier * FIRE_CASCADE_DAMAGE_PER_LEVEL,
                caster.location.add(offsetVector),
                false,
                sound
            )
                .runTaskLater(stepDelay + FIRE_CASCADE_ACTIVATION_DELAY)

            offsetVector.add(addVector)
            i++
        }
    }

    private class CastFireCascadeRunnable(
        private val caster: LivingEntity,
        private val damage: Double,
        private val location: Location,
        private val indicator: Boolean,
        private val sound: Boolean
    ) : BukkitRunnable() {
        override fun run() {
            val world = location.world ?: return

            world.spawnParticle(
                Particle.FLAME,
                location.x, location.y + (if (indicator) 0 else 1), location.z,
                25 + (if (indicator) 0 else 1), 0.2, 0.1 + (if (indicator) 0 else 2), 0.2, 0.0001
            )
            if (indicator) {
                world.spawnParticle(
                    Particle.FLAME,
                    location.x, location.y, location.z,
                    15, 0.2, 0.1, 0.2, 0.0001
                )
                if (sound)
                    world.playSound(location, Sound.ITEM_FIRECHARGE_USE, SoundCategory.AMBIENT, 0.4f, 0.5f)
            } else {
                world.spawnParticle(
                    Particle.FLAME,
                    location.x, location.y + 1, location.z,
                    50, 0.2, 0.8, 0.2, 0.0001
                )
                if (sound)
                    world.playSound(location, Sound.ITEM_FIRECHARGE_USE, SoundCategory.AMBIENT, 0.7f, 1f)
                dealDamage()
            }
            this.cancel()
        }

        private fun dealDamage() {
            val world = location.world ?: return
            for (player in world.players) {
                val pLoc = player.location
                if (sqrt((location.x - pLoc.x).pow(2) + (location.z - pLoc.z).pow(2)) < 1) {
                    player.damage(damage, caster)
                    player.fireTicks = FIRE_CASCADE_FIRE_TICKS
                }
            }
        }
    }
}