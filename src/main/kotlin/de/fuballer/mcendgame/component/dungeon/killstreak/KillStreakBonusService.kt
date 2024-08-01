package de.fuballer.mcendgame.component.dungeon.killstreak

import de.fuballer.mcendgame.component.dungeon.modifier.ModifierUtil.addModifier
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierUtil.removeModifiersBySource
import de.fuballer.mcendgame.event.KillStreakCreatedForPlayerEvent
import de.fuballer.mcendgame.event.KillStreakRemovedEvent
import de.fuballer.mcendgame.event.KillStreakUpdatedEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class KillStreakBonusService : Listener {
    @EventHandler
    fun on(event: KillStreakCreatedForPlayerEvent) {
        val player = event.player

        val killStreakModifier = KillStreakSettings.createKillStreakModifier(event.streak)
        player.addModifier(killStreakModifier)
    }

    @EventHandler
    fun on(event: KillStreakUpdatedEvent) {
        val killStreakModifier = KillStreakSettings.createKillStreakModifier(event.streak)

        for (player in event.players) {
            player.removeModifiersBySource(KillStreakSettings.KILLSTREAK_MODIFIER_SOURCE)
            player.addModifier(killStreakModifier)
        }
    }

    @EventHandler
    fun on(event: KillStreakRemovedEvent) {
        val player = event.player

        player.removeModifiersBySource(KillStreakSettings.KILLSTREAK_MODIFIER_SOURCE)
    }
}