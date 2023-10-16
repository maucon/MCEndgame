package de.fuballer.mcendgame.component.dungeon.leave

import de.fuballer.mcendgame.component.dungeon.leave.db.DungeonLeaveRepository
import de.fuballer.mcendgame.domain.Portal
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerRespawnEvent

class DungeonLeaveService(
    private val dungeonLeaveRepo: DungeonLeaveRepository
) : Service {
    fun onPlayerEntityInteract(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked as? ArmorStand ?: return
        if (!dungeonLeaveRepo.exists(entity.world.name)) return

        val portal = getPortal(entity) ?: return
        val player = event.player

        val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(player)
        EventGateway.apply(playerDungeonLeaveEvent)

        portal.teleportPlayer(player, false)
    }

    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val world = player.world

        val dungeonLeave = dungeonLeaveRepo.findById(world.name) ?: return
        event.respawnLocation = dungeonLeave.leaveLocation

        if (player.gameMode == GameMode.ADVENTURE) {
            player.gameMode = GameMode.SURVIVAL
        }
    }

    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (entity !is Player) return

        val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(entity)
        EventGateway.apply(playerDungeonLeaveEvent)
    }

    fun onDungeonWorldDelete(event: DungeonWorldDeleteEvent) {
        dungeonLeaveRepo.delete(event.world.name)
    }

    fun createPortal(
        worldName: String,
        portalLocation: Location,
        active: Boolean
    ) {
        val dungeonLeave = dungeonLeaveRepo.getById(worldName)

        val portal = Portal(portalLocation, dungeonLeave.leaveLocation, active)
        dungeonLeave.portals.add(portal)

        dungeonLeaveRepo.save(dungeonLeave)
    }

    fun onDungeonComplete(event: DungeonCompleteEvent) {
        val dungeonLeave = dungeonLeaveRepo.findById(event.world.name) ?: return
        dungeonLeave.portals.forEach { it.activate() }
    }

    private fun getPortal(entity: Entity) =
        dungeonLeaveRepo.findById(entity.world.name)
            ?.portals
            ?.find { it.portalEntityId == entity.uniqueId }
}
