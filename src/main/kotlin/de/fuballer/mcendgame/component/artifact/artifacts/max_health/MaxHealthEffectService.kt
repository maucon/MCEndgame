package de.fuballer.mcendgame.component.artifact.artifacts.max_health

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class MaxHealthEffectService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = player.getHighestArtifactTier(MaxHealthArtifactType) ?: return

        val (addedHealth) = MaxHealthArtifactType.getValues(tier)
        val realHealth = 20.0 + addedHealth

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = realHealth
        player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value!!
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
    }
}