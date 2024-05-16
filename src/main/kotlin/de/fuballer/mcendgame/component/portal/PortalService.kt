package de.fuballer.mcendgame.component.portal

import de.fuballer.mcendgame.component.portal.db.Portal
import de.fuballer.mcendgame.component.portal.db.PortalRepository
import de.fuballer.mcendgame.component.portal.skins.DefaultPortalSkin
import de.fuballer.mcendgame.component.portal.skins.PortalSkin
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.event.PortalFailedEvent
import de.fuballer.mcendgame.event.PortalUsedEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.extension.EntityExtension.isPortal
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.plugin.java.JavaPlugin

@Component
class PortalService(
    private val portalRepo: PortalRepository,
    private val server: Server
) : Listener, LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) {
        server.worlds
            .filter { !it.isDungeonWorld() }
            .flatMap { it.entities }
            .filter { it.isPortal() }
            .forEach { it.remove() }
    }

    @EventHandler
    fun on(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked as? ArmorStand ?: return
        if (!entity.isPortal()) return

        val player = event.player
        val portal = portalRepo.getById(entity.uniqueId)

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
        if (location.world == null
            || target.world == null
        ) {
            throw IllegalArgumentException("world of locations cannot be null")
        }

        val portal = Portal(location, target, isSingleUse, skin)
        portalRepo.save(portal)

        return portal
    }

    private fun failPortalUsage(player: Player, portal: Portal) {
        player.sendMessage(PortalSettings.INSTANCE_CLOSED)

        val failedEvent = PortalFailedEvent(portal, player)
        EventGateway.apply(failedEvent)
    }
}