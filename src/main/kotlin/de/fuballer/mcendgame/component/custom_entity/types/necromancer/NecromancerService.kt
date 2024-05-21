package de.fuballer.mcendgame.component.custom_entity.types.necromancer

import de.fuballer.mcendgame.component.custom_entity.types.deathball.DeathBallEntityType
import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EnemyUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Snowball
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

        event.entity.swingMainHand()

        val arrow = event.projectile as? Arrow ?: return
        ShootDeathBall(event.entity, arrow).runTaskLater(PluginConfiguration.INSTANCE, baseAttackDelay)

        event.cancel()
    }

    private class ShootDeathBall(
        private val necromancer: LivingEntity,
        private val arrow: Arrow
    ) : BukkitRunnable() {
        override fun run() {
            val deathBall = EnemyUtil.shootCustomProjectile(necromancer, arrow, EntityType.SNOWBALL, Sound.ITEM_FIRECHARGE_USE) as Snowball
            deathBall.customName = DeathBallEntityType.customName
        }
    }
}
