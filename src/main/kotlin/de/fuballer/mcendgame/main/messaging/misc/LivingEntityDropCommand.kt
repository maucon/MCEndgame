package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

/**
 * server-side only
 */
data class LivingEntityDropCommand(
    val world: Level,
    val entity: LivingEntity,
    val causedByPlayer: Boolean,

    var dropLoot: Boolean = true,
    var dropEquipment: Boolean = true,
    var dropInventory: Boolean = true,
    var dropExperience: Boolean = true,
) {
    constructor(entity: LivingEntity, causedByPlayer: Boolean)
            : this(entity.level(), entity, causedByPlayer)
}