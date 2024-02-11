package de.fuballer.mcendgame.component.dungeon.damage

import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.Listener

@Component
class FriendlyFireService : Listener {
//    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
//    fun on(event: DamageCalculationEvent) {
//        if (WorldUtil.isNotDungeonWorld(event.damager.world)) return
//
//        // disable player to player friendly fire
//        if (event.damager is Player && event.damaged is Player) {
//            event.isCancelled = true
//            return
//        }
//
//        // disable enemy to enemy friendly fire
//        if (event.damager.isEnemy() && event.damaged.isEnemy()) {
//            event.isCancelled = true
//            return
//        }
//    }
}