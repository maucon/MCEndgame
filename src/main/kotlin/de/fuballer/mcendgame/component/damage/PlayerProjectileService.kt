package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.setAddedBaseDamage
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class PlayerProjectileService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: EntityShootBowEvent) {
        val projectile = event.projectile as? AbstractArrow ?: return
        val player = event.entity as? Player ?: return

        val addedDamage = getAddedDamage(player)
        projectile.setAddedBaseDamage(addedDamage)
    }

    private fun getAddedDamage(player: Player): Double {
        val baseDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: return 0.0
        val totalDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return 0.0
        val addedDamage = totalDamage - baseDamage

        val strengthDamage = DamageUtil.getStrengthDamage(player)
        return addedDamage - strengthDamage
    }
}