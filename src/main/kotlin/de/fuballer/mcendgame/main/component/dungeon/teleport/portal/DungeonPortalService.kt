package de.fuballer.mcendgame.main.component.dungeon.teleport.portal

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBrokenEvent
import de.fuballer.mcendgame.main.component.dungeon.world.DungeonWorldClosedEvent
import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.component.portal.Portals
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.fuballer.mcendgame.main.component.portal.type.DefaultPortalType
import de.fuballer.mcendgame.main.component.portal.type.PortalType
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGeneratedEvent
import de.fuballer.mcendgame.main.messaging.dungeon.OpenDungeonButtonPressedEvent
import de.fuballer.mcendgame.main.messaging.dungeon.OpenTrainingDungeonButtonPressedEvent
import de.fuballer.mcendgame.main.util.extension.BlockPosExtension.toVec3d
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.toCenter
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getDungeonBossSpawnPosition
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.block.entity.BlockEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

@Injectable
class DungeonPortalService(
    private val dungeonPortalRepo: DungeonPortalRepository
) {
    @EventSubscriber(sync = true)
    fun on(event: DungeonGeneratedEvent) {
        val startPos = event.startPos
        val centeredSpawnPos = Vec3d.of(startPos.pos).add(0.5, 0.0, 0.5)
        val portalType = DefaultPortalType() // TODO get from player

        val deviceCenterPos = event.dungeonDevicePos.toVec3d().add(0.5, 0.0, 0.5)

        val dungeonWorld = event.dungeonWorld
        val leaveLocation = TeleportLocation(event.originWorld, deviceCenterPos.add(0.0, 1.0, 0.0))
        spawnLeavePortal(leaveLocation, centeredSpawnPos, portalType, dungeonWorld)

        val deviceId = event.dungeonDevicePos.hashCode()
        val dungeonTeleportLocation = TeleportLocation(dungeonWorld, centeredSpawnPos, 0f, startPos.rot.toFloat())

        RuntimeConfig.SERVER.execute {
            val portals = createEntryPortals(deviceCenterPos, portalType, event.originWorld, dungeonTeleportLocation)
                .toMutableList()

            val entity = DungeonPortalEntity(deviceId, dungeonWorld, leaveLocation, portals)
            dungeonPortalRepo.save(entity)
        }
    }

    @EventSubscriber(sync = true)
    fun on(event: OpenDungeonButtonPressedEvent) {
        clearOldPortalsOnOpenDungeon(event.dungeonDeviceBlockEntity)
    }

    @EventSubscriber(sync = true)
    fun on(event: OpenTrainingDungeonButtonPressedEvent) {
        clearOldPortalsOnOpenDungeon(event.dungeonDeviceBlockEntity)
    }

    fun clearOldPortalsOnOpenDungeon(blockEntity: BlockEntity) {
        val id = blockEntity.pos.hashCode()
        val entity = dungeonPortalRepo.findById(id) ?: return
        clearPortalsAndDeleteEntity(entity)
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonBossDeathEvent) {
        if (event.isClient) return

        val world = event.world as ServerWorld
        if (!event.world.isDungeonWorld()) return

        val spawnPosition = event.bossEntity.getDungeonBossSpawnPosition()
        val dungeonPortalEntity = dungeonPortalRepo.findByDungeonWorld(world) ?: return

        RuntimeConfig.SERVER.execute {
            Portals.spawn(world, spawnPosition.pos.toCenter(), dungeonPortalEntity.leaveLocation, rotation = spawnPosition.rot.toFloat())
        }
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonWorldClosedEvent) {
        val entity = dungeonPortalRepo.findByDungeonWorld(event.dungeonWorld) ?: return
        clearPortalsAndDeleteEntity(entity)
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonDeviceBrokenEvent) {
        val id = event.blockEntity.pos.hashCode()
        val entity = dungeonPortalRepo.findById(id) ?: return
        clearPortalsAndDeleteEntity(entity)
    }

    private fun spawnLeavePortal(
        leaveLocation: TeleportLocation,
        spawnPos: Vec3d,
        portalType: PortalType,
        dungeonWorld: ServerWorld
    ) {
        val portalLocation = spawnPos.subtract(0.5, 0.0, 0.0)
        RuntimeConfig.SERVER.execute {
            Portals.spawn(dungeonWorld, portalLocation, leaveLocation, type = portalType, lookAt = spawnPos)
        }
    }

    private fun createEntryPortals(
        devicePos: Vec3d,
        portalType: PortalType,
        originWorld: ServerWorld,
        dungeonTeleportLocation: TeleportLocation
    ): MutableList<PortalEntity> {
        val angle = Math.toRadians(360.0 / 6).toFloat()
        var offset = Vec3d(3.0, 0.0, 0.0)

        val portals = mutableListOf<PortalEntity>()
        repeat(6) {
            val portalPos = devicePos.add(offset)
            val portal = Portals.spawn(originWorld, portalPos, dungeonTeleportLocation, type = portalType, lookAt = devicePos, singleUse = true)

            portals.add(portal)
            offset = offset.rotateY(angle)
        }

        return portals
    }

    private fun clearPortalsAndDeleteEntity(dungeonPortalEntity: DungeonPortalEntity) {
        val world = dungeonPortalEntity.leaveLocation.world

        dungeonPortalEntity.portals.onEach {
            it.discard()
        }.clear()

        dungeonPortalRepo.delete(dungeonPortalEntity)
    }
}