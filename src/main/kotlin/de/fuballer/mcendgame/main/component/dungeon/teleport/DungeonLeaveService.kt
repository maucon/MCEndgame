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
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.GlobalPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget
import net.minecraft.world.WorldProperties

@Injectable
class DungeonLeaveService {
    @Initializer
    fun onPlayerJoin() = ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
        val player = handler.player
        if (!player.isInsideDungeon()) return@register
        player.setInsideDungeon(false)

        val world = player.entityWorld
        if (world !is ServerWorld) return@register
        if (world.isDungeonWorld() && teleportToDungeonExitPos(player, world)) return@register

        val respawnTarget = player.getRespawnTarget(true) {}
        player.teleportTo(respawnTarget)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        event.player.setInsideDungeon(event.newWorld.isDungeonWorld())
    }

    @CommandHandler
    fun on(cmd: GetRespawnCommand) {
        val world = cmd.player.entityWorld
        if (!world.isDungeonWorld()) return
        if (world !is ServerWorld) return

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