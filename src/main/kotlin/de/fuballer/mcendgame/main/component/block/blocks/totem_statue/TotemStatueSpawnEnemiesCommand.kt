package de.fuballer.mcendgame.main.component.block.blocks.totem_statue

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

data class TotemStatueSpawnEnemiesCommand(
    val world: ServerLevel,
    val positions: List<BlockPos>,
    var enemies: List<LivingEntity> = listOf(),
)