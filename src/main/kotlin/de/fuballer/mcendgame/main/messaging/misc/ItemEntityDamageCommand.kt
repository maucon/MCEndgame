package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.Level

data class ItemEntityDamageCommand(
    val world: Level,
    val entity: ItemEntity,
    val source: DamageSource,
    var ignoresDamage: Boolean = false,
) {
    companion object {
        fun of(entity: ItemEntity, source: DamageSource) = ItemEntityDamageCommand(entity.level(), entity, source)
    }
}