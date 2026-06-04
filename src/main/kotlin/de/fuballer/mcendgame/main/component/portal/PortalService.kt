package de.fuballer.mcendgame.main.component.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportExtensions.teleportTo
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class PortalService {
    @EventSubscriber(sync = true)
    fun on(event: PortalUsedEvent) {
        val player = event.player

        val teleportSuccessful = event.teleportLocation
            ?.let { player.teleportTo(it) }
            ?: false

        if (!teleportSuccessful) {
            player.displayClientMessage(PortalSettings.TELEPORTATION_FAILED_MESSAGE, false)
        }
    }
}