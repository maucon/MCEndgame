package de.fuballer.mcendgame.component.dungeon.damage

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Component
class FriendlyFireService : Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return

        // disable player to player friendly fire
        if (event.damager is Player && event.damaged is Player) {
            event.isCancelled = true
            return
        }

        // disable enemy to enemy friendly fire
        if (event.damager.isEnemy() && event.damaged.isEnemy()) {
            event.isCancelled = true
            return
        }
    }
}