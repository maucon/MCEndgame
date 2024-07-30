package de.fuballer.mcendgame.component.totem.totems.projectile_damage

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Component
class ProjectileDamageEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) { // FIXME add to player attributes
        if (!event.isDungeonWorld) return
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) return

        val player = EntityUtil.getPlayerDamager(event.damager) ?: return
        val tier = player.getHighestTotemTier(ProjectileDamageTotemType) ?: return

        val (incDmg) = ProjectileDamageTotemType.getValues(tier)
        event.increasedDamage.add(incDmg)
    }
}