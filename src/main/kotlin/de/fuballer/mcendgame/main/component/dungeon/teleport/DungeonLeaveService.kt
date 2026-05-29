package de.fuballer.mcendgame.main.component.dungeon.teleport

import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.util.extension.BlockPosExtension.toVec3d
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.isInsideDungeon
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonExitLocation
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.clearDungeonExitLocation
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.setDungeonExitLocation
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.setInsideDungeon
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonExitPos
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget

@Injectable
class DungeonLeaveService {
    @Initializer
    fun afterPlayerRespawn() = ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, alive ->
        val oldWorld = oldPlayer.entityWorld
        if (!oldWorld.isDungeonWorld()) return@register

        oldPlayer.getDungeonExitLocation()?.let { newPlayer.setDungeonExitLocation(it) }
        teleportToDungeonExitPos(newPlayer, oldWorld)
    }

    @Initializer
    fun onPlayerJoin() = ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
        val player = handler.player
        if (!player.isInsideDungeon()) return@register
        player.setInsideDungeon(false)

        val world = player.entityWorld
        if (world.isDungeonWorld() && teleportToDungeonExitPos(player, world)) return@register

        val respawnTarget = player.getRespawnTarget(true) {}
        player.teleportTo(respawnTarget)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        event.player.setInsideDungeon(event.newWorld.isDungeonWorld())
    }

    private fun teleportToDungeonExitPos(
        player: PlayerEntity,
        dungeonWorld: ServerWorld,
    ): Boolean {
        val dungeonExitLocation = player.getDungeonExitLocation()
        if (dungeonExitLocation != null) {
            val targetWorld = RuntimeConfig.SERVER.getWorld(dungeonExitLocation.world.registryKey)
            if (targetWorld != null) {
                val teleportTarget = TeleportTarget(
                    targetWorld,
                    dungeonExitLocation.coordinates,
                    Vec3d.ZERO,
                    dungeonExitLocation.yRot,
                    dungeonExitLocation.xRot,
                ) {}

                player.teleportTo(teleportTarget)
                player.clearDungeonExitLocation()
                return true
            }
        }

        val exitPos = dungeonWorld.getDungeonExitPos()
        val targetWorld = RuntimeConfig.SERVER.getWorld(exitPos.dimension) ?: return false

        val teleportTarget = TeleportTarget(
            targetWorld,
            exitPos.pos.toVec3d().add(0.5, 1.0, 0.5),
            Vec3d.ZERO,
            0.0F,
            0.0F,
        ) {}

        player.teleportTo(teleportTarget)
        return true
    }
}