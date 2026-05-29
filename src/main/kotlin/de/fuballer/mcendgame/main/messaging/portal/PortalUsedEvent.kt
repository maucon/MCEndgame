package de.fuballer.mcendgame.main.messaging.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import net.minecraft.entity.player.PlayerEntity

data class PortalUsedEvent(
    val player: PlayerEntity,
    val sourceTeleportLocation: TeleportLocation?,
    val teleportLocation: TeleportLocation?
)