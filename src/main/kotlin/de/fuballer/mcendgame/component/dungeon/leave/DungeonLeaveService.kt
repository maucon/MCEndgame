package de.fuballer.mcendgame.component.dungeon.leave

import de.fuballer.mcendgame.component.dungeon.leave.db.DungeonLeaveRepository
import de.fuballer.mcendgame.component.portal.db.Portal
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerRespawnEvent

@Component
class DungeonLeaveService(
    private val dungeonLeaveRepo: DungeonLeaveRepository
) : Listener {
    @EventHandler
    fun on(event: PlayerChangedWorldEvent) {
        if (!event.from.isDungeonWorld()) return

        val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(event.player)
        EventGateway.apply(playerDungeonLeaveEvent)
    }

    @EventHandler
    fun on(event: PlayerRespawnEvent) {
        val player = event.player
        val world = player.world

        val dungeonLeave = dungeonLeaveRepo.findById(world.name) ?: return
        event.respawnLocation = dungeonLeave.leaveLocation
    }

    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        if (entity !is Player) return

        val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(entity)
        EventGateway.apply(playerDungeonLeaveEvent)
    }

    @EventHandler
    fun on(event: DungeonWorldDeleteEvent) {
        dungeonLeaveRepo.delete(event.world.name)
    }

    @EventHandler
    fun on(event: DungeonCompleteEvent) {
        val dungeonLeave = dungeonLeaveRepo.findById(event.world.name) ?: return
        dungeonLeave.portals.forEach { it.open() }
    }

    fun createPortal(
        worldName: String,
        portalLocation: Location,
        active: Boolean
    ) {
        val dungeonLeave = dungeonLeaveRepo.getById(worldName)
        val portal = Portal(portalLocation, dungeonLeave.leaveLocation, isInitiallyActive = active)

        dungeonLeave.portals.add(portal)
        dungeonLeaveRepo.save(dungeonLeave)
    }
}
