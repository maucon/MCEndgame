package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

data class PlayerEntityDeathEvent(
    val isClient: Boolean,
    val world: Level,
    val player: Player,
    val killer: LivingEntity?,
)