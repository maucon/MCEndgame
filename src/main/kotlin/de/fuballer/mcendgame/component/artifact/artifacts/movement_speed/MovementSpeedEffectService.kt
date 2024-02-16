package de.fuballer.mcendgame.component.artifact.artifacts.movement_speed

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

@Component
class MovementSpeedEffectService : Listener {
    @EventHandler
    fun on(event: PlayerJoinEvent) {
        if (event.player.world.isDungeonWorld()) {
            processJoin(event.player)
        } else {
            processLeave(event.player)
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        processJoin(player)
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player
        processLeave(player)
    }

    private fun processJoin(player: Player) {
        val tier = player.getHighestArtifactTier(MovementSpeedArtifactType) ?: return

        val (speedMultiplier) = MovementSpeedArtifactType.getValues(tier)
        val realSpeedMultiplier = 1 + speedMultiplier

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1 * realSpeedMultiplier
    }

    private fun processLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1
    }
}