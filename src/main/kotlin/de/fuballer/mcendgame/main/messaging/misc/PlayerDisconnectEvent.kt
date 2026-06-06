package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerPlayer

data class PlayerDisconnectEvent(
    val player: ServerPlayer,
)