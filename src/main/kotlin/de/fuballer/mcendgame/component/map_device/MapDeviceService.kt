package de.fuballer.mcendgame.component.map_device

import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationService
import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressService
import de.fuballer.mcendgame.component.map_device.db.MapDeviceEntity
import de.fuballer.mcendgame.component.map_device.db.MapDeviceRepository
import de.fuballer.mcendgame.component.portal.PortalService
import de.fuballer.mcendgame.event.DungeonOpenEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.event.PortalFailedEvent
import de.fuballer.mcendgame.event.PortalUsedEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.MathUtil
import de.fuballer.mcendgame.util.ThreadUtil.async
import de.fuballer.mcendgame.util.ThreadUtil.bukkitSync
import org.bukkit.Location
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.logging.Logger
import kotlin.math.min
import kotlin.time.measureTime

@Service
class MapDeviceService(
    private val mapDeviceRepo: MapDeviceRepository,
    private val dungeonGenerationService: DungeonGenerationService,
    private val playerDungeonProgressService: PlayerDungeonProgressService,
    private val portalService: PortalService,
    private val logger: Logger
) : Listener, LifeCycleListener {
    override fun terminate() {
        mapDeviceRepo.findAll()
            .forEach { entity ->
                entity.portals.forEach { it.close() }
                entity.portals.clear()

                mapDeviceRepo.save(entity)
            }
    }

    @EventHandler
    fun on(event: PortalUsedEvent) {
        val portal = event.portal
        val mapDevice = mapDeviceRepo.findByPortal(portal) ?: return

        mapDevice.portals.remove(portal)
        mapDeviceRepo.save(mapDevice)

        updateMapDeviceVisual(mapDevice)
    }

    @EventHandler
    fun on(event: PortalFailedEvent) {
        val mapDevice = mapDeviceRepo.findByPortal(event.portal) ?: return

        closeDungeon(mapDevice)
    }

    fun openDungeon(
        mapDevice: MapDeviceEntity,
        player: Player
    ) = async {
        val time = measureTime {
            val mapTier = playerDungeonProgressService.getPlayerDungeonLevel(player.uniqueId).tier
            val leaveLocation = mapDevice.location.clone().add(0.5, 1.0, 0.5)
            val startLocation = dungeonGenerationService.generateDungeon(player, mapTier, leaveLocation)

            val dungeonOpenEvent = DungeonOpenEvent(player, startLocation.world!!)
            EventGateway.apply(dungeonOpenEvent)

            closeDungeon(mapDevice)
            openPortals(mapDevice, startLocation)
            updateMapDeviceVisual(mapDevice)
        }

        logger.info("Created Dungeon in ${time.inWholeMilliseconds} ms")
    }

    fun closeDungeon(entity: MapDeviceEntity) {
        entity.portals.forEach { it.close() }
        entity.portals.clear()

        mapDeviceRepo.save(entity)
        updateMapDeviceVisual(entity)
    }

    private fun openPortals(mapDevice: MapDeviceEntity, target: Location) {
        val deviceCenter = mapDevice.location.clone().add(0.5, 0.0, 0.5)

        mapDevice.portals = MapDeviceSettings.PORTAL_OFFSETS
            .map { deviceCenter.clone().add(it) }
            .onEach { it.yaw = MathUtil.calculateYawToFacingLocation(it, deviceCenter) + 180 }
            .map { portalService.createPortal(it, target, isSingleUse = true) }
            .toMutableList()

        mapDeviceRepo.save(mapDevice)
    }

    private fun updateMapDeviceVisual(mapDevice: MapDeviceEntity) {
        val block = mapDevice.location.block
        val anchor = block.blockData as RespawnAnchor

        anchor.charges = min(4, mapDevice.portals.size)
        bukkitSync { block.blockData = anchor }
    }
}