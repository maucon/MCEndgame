package de.fuballer.mcendgame.component.custom_entity.types.harpy

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.EnemyUtil
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class HarpyService : Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val entity = event.entity
        if (entity.getCustomEntityType() != HarpyEntityType) return
        val projectile = event.projectile as? AbstractArrow ?: return

        EnemyUtil.shootCustomProjectile(entity, projectile, EntityType.SNOWBALL, Sound.ENTITY_SNOWBALL_THROW) as Snowball

        event.isCancelled = true
    }
}