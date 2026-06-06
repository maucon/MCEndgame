package de.fuballer.mcendgame.main.messaging.dungeon

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.Level

data class DungeonFinalBossDeathEvent(
    val isClient: Boolean,
    val world: Level,
    val bossEntity: Mob,
    val killer: LivingEntity?,
) {
    companion object {
        fun of(e: DungeonBossDeathEvent) = DungeonFinalBossDeathEvent(e.isClient, e.world, e.bossEntity, e.killer)
    }
}