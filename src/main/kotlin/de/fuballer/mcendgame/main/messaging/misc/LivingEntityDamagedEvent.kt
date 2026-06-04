package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

data class LivingEntityDamagedEvent(
    val damaged: LivingEntity,
    val damageSource: DamageSource,
    /**
     * damage amount after mitigation
     */
    val amount: Float,
)