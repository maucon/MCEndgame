package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.server.network.ServerPlayerEntity

/**
 * only server-side
 */
data class OpenTrainingDungeonButtonPressedEvent(
    val blockEntity: BlockEntity,
    val player: ServerPlayerEntity,
    val dungeonDeviceBlockEntity: DungeonDeviceBlockEntity,
)