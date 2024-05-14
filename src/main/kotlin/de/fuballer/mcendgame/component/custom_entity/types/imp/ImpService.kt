package de.fuballer.mcendgame.component.custom_entity.types.imp

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EnemyUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

@Component
class ImpService : Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onEntityShootBow(event: ProjectileLaunchEvent) {
        val projectile = event.entity as? AbstractArrow ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return

        if (shooter.getCustomEntityType() != ImpEntityType) return

        val fireball = EnemyUtil.shootCustomProjectile(shooter, projectile, EntityType.SMALL_FIREBALL, Sound.ITEM_FIRECHARGE_USE) as Fireball

        event.cancel()

        val creature = shooter as? Creature ?: return
        val target = creature.target ?: return

        fireball.velocity = target.location.subtract(creature.location).toVector().normalize()
        fireball.direction = fireball.velocity
    }
}