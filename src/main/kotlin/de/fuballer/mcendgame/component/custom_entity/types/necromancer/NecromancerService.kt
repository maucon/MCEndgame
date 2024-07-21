package de.fuballer.mcendgame.component.custom_entity.types.necromancer

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EnemyUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.scheduler.BukkitRunnable

private const val BASE_ATTACK_DELAY = 10L

@Component
class NecromancerService : Listener {
    @EventHandler
    fun on(event: EntityShootBowEvent) {
        if (event.entity.getCustomEntityType() != NecromancerEntityType) return
        val necromancer = event.entity as? Creature ?: return
        val arrow = event.projectile as? Arrow ?: return
        val target = necromancer.target ?: return

        necromancer.swingMainHand()
        ShootWitherSkull(necromancer, arrow, target).runTaskLater(BASE_ATTACK_DELAY)

        event.cancel()
    }

    private class ShootWitherSkull(
        private val necromancer: LivingEntity,
        private val arrow: Arrow,
        private val target: LivingEntity
    ) : BukkitRunnable() {
        override fun run() {
            val witherSkull = EnemyUtil.shootProjectile(necromancer, arrow, target, EntityType.WITHER_SKULL, Sound.ITEM_FIRECHARGE_USE) as WitherSkull
            witherSkull.direction = witherSkull.velocity
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    private fun onNecromancerDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity.getCustomEntityType() != NecromancerEntityType) return
        val arrow = event.damager as? Arrow ?: return

        if (!NecromancerSettings.doesBatBlockArrow()) return
        event.cancel()
        arrow.remove()

        val location = event.entity.location
        location.add(0.0, 0.25, 0.0)
        val world = location.world ?: return

        for (i in 0 until NecromancerSettings.BATS_AMOUNT) {
            val bat = world.spawnEntity(location, EntityType.BAT)
            bat.velocity = NecromancerSettings.getBatVelocity()

            SchedulingUtil.runTaskLater(NecromancerSettings.getBatDuration()) {
                val batLocation = bat.location
                world.spawnParticle(Particle.CRIT, batLocation.x, batLocation.y, batLocation.z, 10, 0.1, 0.1, 0.1, 0.3)
                world.spawnParticle(Particle.ASH, batLocation.x, batLocation.y, batLocation.z, 25, 0.1, 0.2, 0.1, 1.0)
                bat.remove()
            }
        }
    }
}