package de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

abstract class SummonData(
    val factory: (ServerLevel, LivingEntity, LivingEntity) -> Entity,
) {
    abstract fun apply(
        level: ServerLevel,
        summoner: LivingEntity,
        target: LivingEntity?,
    )
}