package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.setBaseDamage
import org.bukkit.attribute.Attribute
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.potion.PotionEffectType

@Component
class PlayerProjectileService : Listener {
    @EventHandler
    fun on(event: ProjectileLaunchEvent) {
        val projectile = event.entity as? AbstractArrow ?: return
        val player = projectile.shooter as? Player ?: return

        val addedDamage = getAddedDamage(player)

        projectile.setBaseDamage(projectile.damage + addedDamage)
    }

    private fun getAddedDamage(player: Player): Double {
        val baseDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: return 0.0
        val totalDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return 0.0
        var addedDamage = totalDamage - baseDamage

        val strengthEffect = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)
        val strengthEffectLevel = if (strengthEffect == null) 0 else strengthEffect.amplifier + 1
        addedDamage -= (3 * strengthEffectLevel)

        return addedDamage
    }
}