package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

data class ShieldHitEvent(
    val entity: LivingEntity,
    val itemStack: ItemStack,
)