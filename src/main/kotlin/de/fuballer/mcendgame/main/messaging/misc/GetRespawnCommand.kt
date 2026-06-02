package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.network.ServerPlayerEntity

data class GetRespawnCommand(
    val player: ServerPlayerEntity,
    var respawn: ServerPlayerEntity.Respawn?,
) {
    constructor(player: ServerPlayerEntity) : this(player, null) // java
}