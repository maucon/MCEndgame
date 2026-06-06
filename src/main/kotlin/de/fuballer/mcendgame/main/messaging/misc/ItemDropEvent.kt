package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

data class ItemDropEvent(
    val entity: ItemEntity,
    val stack: ItemStack,
    val world: Level,
) {
    companion object {
        fun of(entity: ItemEntity) = ItemDropEvent(entity, entity.item, entity.level())
    }
}