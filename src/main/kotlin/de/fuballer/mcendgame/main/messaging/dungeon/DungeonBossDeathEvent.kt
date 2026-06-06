package de.fuballer.mcendgame.main.messaging.dungeon

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.Level

/**
 * FIXME currently only thrown server-side
 */
data class DungeonBossDeathEvent(
    val isClient: Boolean,
    val world: Level,
    val bossEntity: Mob,
    val killer: LivingEntity?,
)