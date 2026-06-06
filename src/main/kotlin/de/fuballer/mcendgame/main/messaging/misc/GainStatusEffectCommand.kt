package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity

data class GainStatusEffectCommand(
    val entity: LivingEntity,
    var effect: MobEffectInstance,
)