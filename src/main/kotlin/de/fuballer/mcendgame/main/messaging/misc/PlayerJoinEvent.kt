package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerPlayer

data class PlayerJoinEvent(
    var player: ServerPlayer,
)