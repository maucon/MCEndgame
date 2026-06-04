package de.fuballer.mcendgame.main.messaging.dungeon

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

data class DungeonEnemyDeathEvent(
    val isClient: Boolean,
    val world: Level,
    val enemyEntity: LivingEntity,
    val killer: LivingEntity?,
)