package de.fuballer.mcendgame.component.totem.totems.less_damage_taken

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class LessDamageTakenEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) { // FIXME change with #234
        if (!event.isDungeonWorld) return
        val player = event.damaged as? Player ?: return
        val tier = player.getHighestTotemTier(LessDamageTakenTotemType) ?: return

        val (lessDmg) = LessDamageTakenTotemType.getValues(tier)
        event.moreDamage.add(-lessDmg)
    }
}