package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

/**
 * only server-side
 */
data class DungeonGeneratedEvent(
    val player: ServerPlayer,
    val originWorld: ServerLevel,
    val dungeonWorld: ServerLevel,
    val startPos: SpawnPosition,
    val dungeonDevicePos: BlockPos,
    val isTraining: Boolean = false,
)