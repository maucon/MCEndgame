package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

data class LivingEntityDeathEvent(
    val isClient: Boolean,
    val world: Level,
    val entity: LivingEntity,
    val killer: LivingEntity?,
) {
    constructor(entity: LivingEntity)
            : this(entity.level().isClientSide, entity.level(), entity, entity.lastHurtByMob)
}