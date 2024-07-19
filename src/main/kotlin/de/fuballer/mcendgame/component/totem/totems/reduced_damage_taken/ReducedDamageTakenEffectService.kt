package de.fuballer.mcendgame.component.totem.totems.reduced_damage_taken

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class ReducedDamageTakenEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) { // FIXME change with #234
        if (!event.isDungeonWorld) return
        val player = EntityUtil.getPlayerDamager(event.damager) ?: return
        val tier = player.getHighestTotemTier(ReducedDamageTakenTotemType) ?: return

        val (reducedDmg) = ReducedDamageTakenTotemType.getValues(tier)
        event.moreDamage.add(-reducedDmg)
    }
}