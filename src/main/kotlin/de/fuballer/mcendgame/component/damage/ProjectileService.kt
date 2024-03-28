package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.extension.ProjectileExtension.setAddedBaseDamage
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

@Component
class ProjectileService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: ProjectileLaunchEvent) {
        if (event.entity.world.isDungeonWorld()) return // only for non dungeon worlds

        val projectile = event.entity as? AbstractArrow ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return

        val addedDamage = getAddedDamage(shooter)
        projectile.setAddedBaseDamage(addedDamage)
    }

    private fun getAddedDamage(shooter: LivingEntity): Double {
        val attribute = shooter.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) ?: return 0.0

        var damage = attribute.value
        damage -= attribute.baseValue
        damage -= DamageUtil.getStrengthDamage(shooter)

        return damage
    }
}