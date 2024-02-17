package de.fuballer.mcendgame.component.custom_entity.types.imp

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EnemyUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Creature
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class ImpService : Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val entity = event.entity
        if (entity.getCustomEntityType() != ImpEntityType) return
        val projectile = event.projectile as? AbstractArrow ?: return

        val fireball = EnemyUtil.shootCustomProjectile(entity, projectile, EntityType.SMALL_FIREBALL, Sound.ITEM_FIRECHARGE_USE) as Fireball

        event.isCancelled = true

        val creature = entity as? Creature ?: return
        val target = creature.target ?: return

        fireball.velocity = target.location.subtract(creature.location).toVector().normalize()
        fireball.direction = fireball.velocity
    }
}