package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

data class EquipmentChangeEvent(
    var entity: LivingEntity,
    var slot: EquipmentSlot,
    var oldStack: ItemStack,
    var newStack: ItemStack,
)