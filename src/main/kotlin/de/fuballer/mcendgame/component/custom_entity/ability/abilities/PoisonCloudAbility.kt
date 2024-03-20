package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.util.DungeonUtil
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

const val POISON_CLOUD_RANGE = 20.0
const val POISON_CLOUD_DURATION = 100 //in Ticks
const val POISON_CLOUD_RADIUS = 3f
const val POISON_CLOUD_RADIUS_PER_TICK = 0.015f
val POISON_CLOUD_EFFECT = PotionEffect(PotionEffectType.HARM, 0, 1, true)
const val POISON_CLOUD_REAPPLICATION_DELAY = 10 //in Ticks

object PoisonCloudAbility : Ability {
    override fun cast(caster: LivingEntity) {
        DungeonUtil.getNearbyPlayers(caster, POISON_CLOUD_RANGE)
            .forEach {
                val spawnLoc = Location(it.location.world, it.location.x, it.location.blockY.toDouble(), it.location.z)
                while (it.world.getBlockAt(spawnLoc).isPassable) {
                    spawnLoc.subtract(0.0, 1.0, 0.0)
                }
                spawnLoc.add(0.0, 1.0, 0.0)

                val cloud = it.world.spawnEntity(spawnLoc, EntityType.AREA_EFFECT_CLOUD, false) as AreaEffectCloud
                cloud.color = Color.GREEN
                cloud.duration = POISON_CLOUD_DURATION
                cloud.radius = POISON_CLOUD_RADIUS
                cloud.radiusPerTick = POISON_CLOUD_RADIUS_PER_TICK
                cloud.addCustomEffect(POISON_CLOUD_EFFECT, true)
                cloud.reapplicationDelay = POISON_CLOUD_REAPPLICATION_DELAY
                cloud.waitTime = POISON_CLOUD_REAPPLICATION_DELAY
            }
    }
}