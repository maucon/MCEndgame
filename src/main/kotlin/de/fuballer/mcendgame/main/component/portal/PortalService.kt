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

        val command = CommandGateway.apply(
            ResolvePortalTeleportLocationCommand(player, event.portal, event.teleportLocation)
        )
        val resolvedLocation = command.teleportLocation

        val teleportSuccessful = teleport(player, resolvedLocation) || (resolvedLocation != event.teleportLocation && teleport(player, event.teleportLocation))

        if (!teleportSuccessful) {
            player.sendSystemMessage(PortalSettings.TELEPORTATION_FAILED_MESSAGE)
        }
    }

    private fun teleport(player: Player, location: TeleportLocation?): Boolean =
        location?.let { player.teleportTo(it) } ?: false
}