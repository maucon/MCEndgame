package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

data class LivingEntityDodgedEvent(
    val entity: LivingEntity,
    val source: Entity?,
    val attacker: Entity?,
)