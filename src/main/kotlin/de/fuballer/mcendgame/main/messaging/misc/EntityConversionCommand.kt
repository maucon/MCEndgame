package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

data class EntityConversionCommand(
    val world: Level,
    val entity: LivingEntity,
    var canConvert: Boolean = true,
) {
    companion object {
        fun of(entity: LivingEntity) = EntityConversionCommand(entity.level(), entity)
    }
}