package de.fuballer.mcendgame.component.artifact.artifacts.movement_speed

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class MovementSpeedEffectService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = player.getHighestArtifactTier(MovementSpeedArtifactType) ?: return

        val (speedMultiplier) = MovementSpeedArtifactType.getValues(tier)
        val realSpeedMultiplier = 1 + speedMultiplier

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1 * realSpeedMultiplier
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1
    }
}