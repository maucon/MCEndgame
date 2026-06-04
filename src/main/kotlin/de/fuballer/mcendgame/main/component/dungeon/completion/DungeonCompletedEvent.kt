package de.fuballer.mcendgame.main.component.dungeon.completion

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

data class DungeonCompletedEvent(
    val isClient: Boolean,
    val dungeonWorld: ServerLevel,
    val players: List<Player>,
)