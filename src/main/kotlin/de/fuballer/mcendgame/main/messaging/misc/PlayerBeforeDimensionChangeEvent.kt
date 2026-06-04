package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.portal.TeleportTransition

data class PlayerBeforeDimensionChangeEvent(
    val player: Player,
    val world: ServerLevel,
    val target: TeleportTransition,
)