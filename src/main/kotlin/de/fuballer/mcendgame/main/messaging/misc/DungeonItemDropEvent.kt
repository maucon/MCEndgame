package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

data class DungeonItemDropEvent(
    val entity: ItemEntity,
    val stack: ItemStack,
    val world: Level,
) {
    companion object {
        fun of(event: ItemDropEvent) = DungeonItemDropEvent(event.entity, event.stack, event.world)
    }
}