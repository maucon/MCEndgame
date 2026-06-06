package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import net.minecraft.server.level.ServerPlayer

/**
 * only server-side
 */
data class OpenDungeonButtonPressedEvent(
    val dungeonDeviceBlockEntity: DungeonDeviceBlockEntity,
    val player: ServerPlayer,
)