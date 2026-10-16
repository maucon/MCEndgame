package de.fuballer.mcendgame.main.component.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportExtensions.teleportTo
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.fuballer.mcendgame.main.messaging.portal.ResolvePortalTeleportLocationCommand
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.entity.player.Player

@Injectable
class PortalService {
    @EventSubscriber(sync = true)
    fun on(event: PortalUsedEvent) {
        val player = event.player

        // Give handlers the chance to send the player somewhere player-specific (e.g. the dungeon
        // portal a player entered through); falls back to the portal's own destination otherwise.
        val command = CommandGateway.apply(
            ResolvePortalTeleportLocationCommand(player, event.portal, event.teleportLocation)
        )
        val resolvedLocation = command.teleportLocation

        val teleportSuccessful = teleport(player, resolvedLocation) ||
            // The player-specific destination could not be reached; never leave the player stuck.
            (resolvedLocation != event.teleportLocation && teleport(player, event.teleportLocation))

        if (!teleportSuccessful) {
            player.sendSystemMessage(PortalSettings.TELEPORTATION_FAILED_MESSAGE)
        }
    }

    private fun teleport(player: Player, location: TeleportLocation?): Boolean =
        location?.let { player.teleportTo(it) } ?: false
}
