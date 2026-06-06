package de.fuballer.mcendgame.main.messaging.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import net.minecraft.world.entity.player.Player

data class PortalUsedEvent(
    val player: Player,
    val teleportLocation: TeleportLocation?
)