package de.fuballer.mcendgame.main.component.dungeon.teleport

import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.misc.GetRespawnCommand
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.util.extension.BlockPosExtension.toVec3d
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.isInsideDungeon
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.setInsideDungeon
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonExitPos
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.GlobalPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget
import net.minecraft.world.WorldProperties
import java.util.*

@Injectable
class DungeonLeaveService {
    private val playersToProcess = mutableSetOf<UUID>()

    @Initializer
    fun onPlayerJoin() = ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
        playersToProcess += handler.player.uuid
    }

    @Initializer
    fun onServerTick() = ServerTickEvents.END_SERVER_TICK.register { server ->
        if (playersToProcess.isEmpty()) return@register

        val players = playersToProcess.toList()
        playersToProcess.clear()

        for (uuid in players) {
            val player = server.playerManager.getPlayer(uuid) ?: continue
            if (!player.isInsideDungeon()) continue

            val world = player.entityWorld
            if (world.isDungeonWorld() && teleportToDungeonExitPos(player, world)) {
                player.setInsideDungeon(false)
                continue
            }

            val respawnTarget = player.getRespawnTarget(true) {}
            player.teleportTo(respawnTarget)
            player.setInsideDungeon(false)
        }
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        event.player.setInsideDungeon(event.newWorld.isDungeonWorld())
    }

    @CommandHandler
    fun on(cmd: GetRespawnCommand) {
        val world = cmd.player.entityWorld
        if (!world.isDungeonWorld()) return

        val exitPos = world.getDungeonExitPos()
        val actualPos = GlobalPos(exitPos.dimension, exitPos.pos.add(0, 1, 0))
        cmd.respawn = ServerPlayerEntity.Respawn(
            WorldProperties.SpawnPoint(actualPos, 0.0f, 0.0f),
            true
        )
    }

    private fun teleportToDungeonExitPos(
        player: ServerPlayerEntity,
        dungeonWorld: ServerWorld,
    ): Boolean {
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