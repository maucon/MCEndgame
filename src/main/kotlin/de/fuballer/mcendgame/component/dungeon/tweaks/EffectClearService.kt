package de.fuballer.mcendgame.component.dungeon.tweaks

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class EffectClearService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player
        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
    }
}