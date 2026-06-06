package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

data class LookAtEntityGoalCommand(
    val mob: Mob,
    val target: LivingEntity,
    var canLookAt: Boolean = true,
)