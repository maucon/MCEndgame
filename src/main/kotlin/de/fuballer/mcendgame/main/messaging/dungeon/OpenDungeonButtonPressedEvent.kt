package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

/**
 * only server-side
 */
data class OpenDungeonButtonPressedEvent(
    val dungeonDeviceBlockEntity: DungeonDeviceBlockEntity,
    val player: ServerPlayerEntity,
)