package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

data class PlayerAfterDimensionChangeEvent(
    val player: Player,
    val oldWorld: ServerLevel,
    val newWorld: ServerLevel,
)