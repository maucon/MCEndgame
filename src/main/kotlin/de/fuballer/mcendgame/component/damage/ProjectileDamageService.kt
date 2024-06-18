package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

@Component
class ProjectileDamageService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: ProjectileLaunchEvent) {
        val projectile = event.entity as? AbstractArrow ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return
        val entityType = shooter.getCustomEntityType() ?: return
        val mapTier = shooter.getMapTier() ?: return

        projectile.damage = entityType.baseDamage + entityType.damagePerTier * mapTier
    }
}