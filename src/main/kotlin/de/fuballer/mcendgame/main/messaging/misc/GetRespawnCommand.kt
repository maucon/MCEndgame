package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.TeleportTarget

data class GetRespawnCommand(
    val player: ServerPlayerEntity,
    var teleportTarget: TeleportTarget?,
) {
    constructor(player: ServerPlayerEntity) : this(player, null) // java
}