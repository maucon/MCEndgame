package de.fuballer.mcendgame.main.messaging.dungeon

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

data class DungeonEntityDeathEvent(
    val isClient: Boolean,
    val world: Level,
    val entity: LivingEntity,
    val killer: LivingEntity?,
)