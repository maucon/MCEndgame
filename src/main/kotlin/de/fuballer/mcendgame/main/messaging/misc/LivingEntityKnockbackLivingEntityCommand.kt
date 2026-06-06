package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity

data class LivingEntityKnockbackLivingEntityCommand(
    val target: LivingEntity,
    val attacker: LivingEntity,
    var strength: Double,
)