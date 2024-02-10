package de.fuballer.mcendgame.component.dungeon.tweaks

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Component
class FriendlyFireService : Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (WorldUtil.isNotDungeonWorld(event.damager.world)) return

        if (event.damager !is Player) return
        if (event.damaged !is Player) return

        event.isCancelled = true
    }
}