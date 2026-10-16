package de.fuballer.mcendgame.main.messaging.portal

import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import net.minecraft.world.entity.player.Player

/**
 * Asks subscribers to resolve the teleport destination for a used [PortalEntity].
 *
 * [teleportLocation] starts out as the static destination stored on the portal and may be replaced
 * by handlers that want to send the player somewhere player-specific (e.g. the portal they entered
 * a dungeon through). If no handler replaces it, the portal's own destination is used as-is.
 */
data class ResolvePortalTeleportLocationCommand(
    val player: Player,
    val portal: PortalEntity,
    var teleportLocation: TeleportLocation?,
)
