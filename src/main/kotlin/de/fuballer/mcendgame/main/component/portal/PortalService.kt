package de.fuballer.mcendgame.main.component.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportExtensions.teleportTo
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.setDungeonExitLocation
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
            player.sendMessage(PortalSettings.TELEPORTATION_FAILED_MESSAGE, false)
            return
        }

        if (event.teleportLocation?.world?.isDungeonWorld() == true) {
            player.setDungeonExitLocation(event.sourceTeleportLocation)
        }
    }
}