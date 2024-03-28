package de.fuballer.mcendgame.component.dungeon.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.extension.ProjectileExtension.setAddedBaseDamage
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

@Component
class ProjectileService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: ProjectileLaunchEvent) {
        if (!event.entity.world.isDungeonWorld()) return

        val projectile = event.entity as? AbstractArrow ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return

        val addedDamage = getAddedDamage(shooter)
        projectile.setAddedBaseDamage(addedDamage)
    }

    private fun getAddedDamage(entity: LivingEntity): Double {
        var damage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return 0.0

        if (entity is Player) {
            damage -= entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: 0.0
            damage -= DamageUtil.getStrengthDamage(entity)
        }

        return damage
    }
}