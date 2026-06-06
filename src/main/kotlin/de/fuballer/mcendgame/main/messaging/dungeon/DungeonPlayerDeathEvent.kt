package de.fuballer.mcendgame.main.messaging.dungeon

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

data class DungeonPlayerDeathEvent(
    val isClient: Boolean,
    val player: Player,
    val killer: LivingEntity?
)