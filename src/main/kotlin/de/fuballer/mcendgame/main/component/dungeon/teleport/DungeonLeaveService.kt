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
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.core.GlobalPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.portal.TeleportTransition
import net.minecraft.world.level.storage.LevelData
import net.minecraft.world.phys.Vec3

@Injectable
class DungeonLeaveService {
    @Initializer
    fun onPlayerJoin() = ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
        val player = handler.player
        if (!player.isInsideDungeon()) return@register
        player.setInsideDungeon(false)

        val world = player.level()
        if (world.isDungeonWorld() && teleportToDungeonExitPos(player, world)) return@register

        val respawnTarget = player.findRespawnPositionAndUseSpawnBlock(true) {}
        player.teleport(respawnTarget)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        event.player.setInsideDungeon(event.newWorld.isDungeonWorld())
    }

    @CommandHandler
    fun on(cmd: GetRespawnCommand) {
        val world = cmd.player.level()
        if (!world.isDungeonWorld()) return

        val exitPos = world.getDungeonExitPos()
        val actualPos = GlobalPos(exitPos.dimension, exitPos.pos.offset(0, 1, 0))
        cmd.respawn = ServerPlayer.RespawnConfig(
            LevelData.RespawnData(actualPos, 0.0f, 0.0f),
            true
        )
    }

    private fun teleportToDungeonExitPos(
        player: ServerPlayer,
        dungeonWorld: ServerLevel,
    ): Boolean {
        val exitPos = dungeonWorld.getDungeonExitPos()
        val targetWorld = RuntimeConfig.SERVER.getLevel(exitPos.dimension) ?: return false

        val teleportTarget = TeleportTransition(
            targetWorld,
            exitPos.pos.toVec3d().add(0.5, 1.0, 0.5),
            Vec3.ZERO,
            0.0F,
            0.0F,
        ) {}

        player.teleport(teleportTarget)
        return true
    }
}