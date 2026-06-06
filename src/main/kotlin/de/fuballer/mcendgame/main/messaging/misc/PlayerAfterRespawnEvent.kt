package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerPlayer

data class PlayerAfterRespawnEvent(
    val oldPlayer: ServerPlayer,
    val newPlayer: ServerPlayer,
    val alive: Boolean,
)