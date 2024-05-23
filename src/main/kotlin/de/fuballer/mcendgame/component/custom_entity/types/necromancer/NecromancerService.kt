package de.fuballer.mcendgame.component.custom_entity.types.necromancer

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EnemyUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.scheduler.BukkitRunnable

const val baseAttackDelay = 10L

@Component
class NecromancerService : Listener {
    @EventHandler
    fun on(event: EntityShootBowEvent) {
        if (event.entity.getCustomEntityType() != NecromancerEntityType) return
        val necromancer = event.entity as? Creature ?: return

        necromancer.swingMainHand()

        val arrow = event.projectile as? Arrow ?: return
        val target = necromancer.target ?: return

        ShootWitherSkull(necromancer, arrow, target).runTaskLater(baseAttackDelay)

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
}