package de.fuballer.mcendgame.component.portal

import de.fuballer.mcendgame.component.portal.db.Portal
import de.fuballer.mcendgame.component.portal.db.PortalRepository
import de.fuballer.mcendgame.component.portal.skins.DefaultPortalSkin
import de.fuballer.mcendgame.component.portal.skins.PortalSkin
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.event.PortalFailedEvent
import de.fuballer.mcendgame.event.PortalUsedEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EntityExtension.isPortal
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

@Service
class PortalService(
    private val portalRepo: PortalRepository,
    private val server: Server
) : Listener {
    @EventHandler
    fun on(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked as? ArmorStand ?: return
        if (!entity.isPortal()) return

        val player = event.player
        val portal = portalRepo.findById(entity.uniqueId)

        if (portal == null) {
            deleteInvalidPortals(entity.location.world!!)
            return
        }
        if (!server.worlds.contains(portal.target.world)) {
            failPortalUsage(player, portal)
            return
        }

        player.teleport(portal.target)

        val usedEvent = PortalUsedEvent(portal, player)
        EventGateway.apply(usedEvent)

        if (portal.isSingleUse) {
            portal.close()
        }
    }

    fun createPortal(
        location: Location,
        target: Location,
        isSingleUse: Boolean = false,
        skin: PortalSkin = DefaultPortalSkin()
    ): Portal {
        require(location.world != null) { "world of location cannot be null" }
        require(target.world != null) { "world of target cannot be null" }

        val portal = Portal(location, target, isSingleUse, skin)
        portalRepo.save(portal)

        return portal
    }

    private fun failPortalUsage(player: Player, portal: Portal) {
        player.sendMessage(PortalSettings.INSTANCE_CLOSED)

        val failedEvent = PortalFailedEvent(portal, player)
        EventGateway.apply(failedEvent)
    }

    private fun deleteInvalidPortals(world: World) {
        world.entities
            .filter { it.isPortal() }
            .filter { !portalRepo.exists(it.uniqueId) }
            .forEach { it.remove() }
    }
}