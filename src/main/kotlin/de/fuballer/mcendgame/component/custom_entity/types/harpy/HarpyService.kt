package de.fuballer.mcendgame.component.custom_entity.types.harpy

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EnemyUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

@Component
class HarpyService : Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun on(event: ProjectileLaunchEvent) {
        val projectile = event.entity as? AbstractArrow ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return

        if (shooter.getCustomEntityType() != HarpyEntityType) return
        EnemyUtil.shootCustomProjectile(shooter, projectile, EntityType.SNOWBALL, Sound.ENTITY_SNOWBALL_THROW) as Snowball

        event.isCancelled = true
    }
}