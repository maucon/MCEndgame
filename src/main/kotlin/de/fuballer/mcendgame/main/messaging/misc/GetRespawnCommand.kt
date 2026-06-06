package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerPlayer

data class GetRespawnCommand(
    val player: ServerPlayer,
    var respawn: ServerPlayer.RespawnConfig?,
) {
    constructor(player: ServerPlayer) : this(player, null) // java
}