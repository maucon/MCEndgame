package de.fuballer.mcendgame.component.map_device

import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationService
import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressService
import de.fuballer.mcendgame.component.map_device.db.MapDeviceEntity
import de.fuballer.mcendgame.component.map_device.db.MapDeviceRepository
import de.fuballer.mcendgame.component.portal.db.Portal
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import org.bukkit.Location
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.math.min

@Component
class MapDeviceService(
    private val mapDeviceRepo: MapDeviceRepository,
    private val dungeonGenerationService: DungeonGenerationService,
    private val playerDungeonProgressService: PlayerDungeonProgressService
) : Listener, LifeCycleListener {
    override fun terminate() {
        mapDeviceRepo.findAll()
            .forEach { closeDungeon(it) }
    }

    @EventHandler
    fun on(event: PortalUsedEvent) {
        val portal = event.portal
        val mapDevice = mapDeviceRepo.findByPortalEntity(portal.entity) ?: return

        val playerDungeonJoinEvent = PlayerDungeonJoinEvent(event.player, portal.target.world!!, portal.target)
        EventGateway.apply(playerDungeonJoinEvent)

        if (!event.portal.isSingleUse) return

        mapDevice.portals.remove(portal)
        mapDeviceRepo.save(mapDevice)

        updateMapDeviceVisual(mapDevice)
    }

    @EventHandler
    fun on(event: PortalFailedEvent) {
        val mapDevice = mapDeviceRepo.findByPortalEntity(event.portal.entity) ?: return

        closeDungeon(mapDevice)
    }

    fun openDungeon(
        mapDevice: MapDeviceEntity,
        player: Player
    ) {
        val mapTier = playerDungeonProgressService.getPlayerDungeonLevel(player.uniqueId).tier
        val leaveLocation = mapDevice.location.clone().add(0.5, 1.0, 0.5)
        val teleportLocation = dungeonGenerationService.generateDungeon(player, mapTier, leaveLocation)

        val dungeonOpenEvent = DungeonOpenEvent(player, teleportLocation.world!!)
        EventGateway.apply(dungeonOpenEvent)

        closeDungeon(mapDevice)
        openPortals(mapDevice, teleportLocation)
        updateMapDeviceVisual(mapDevice)
    }

    fun closeDungeon(entity: MapDeviceEntity) {
        entity.portals.forEach { it.close() }
        entity.portals.clear()

        mapDeviceRepo.save(entity)
        updateMapDeviceVisual(entity)
    }

    private fun openPortals(mapDevice: MapDeviceEntity, target: Location) {
        val deviceLocation = mapDevice.location
        val facing = deviceLocation.clone().add(0.5, 0.0, 0.5).toVector()

        mapDevice.portals = MapDeviceSettings.PORTAL_OFFSETS
            .map { deviceLocation.clone().add(it) }
            .map { Portal(it, target, facing, isInitiallyActive = true, isSingleUse = true) }
            .toMutableList()

        mapDeviceRepo.save(mapDevice)
    }

    private fun updateMapDeviceVisual(mapDevice: MapDeviceEntity) {
        val block = mapDevice.location.block
        val anchor = block.blockData as RespawnAnchor

        anchor.charges = min(4, mapDevice.portals.size)
        block.blockData = anchor
    }
}