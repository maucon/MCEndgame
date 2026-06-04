package de.fuballer.mcendgame.main.component.damage.ignore_damage

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

data class IgnoreDamageCommand(
    val world: Level,
    val entity: LivingEntity,
    val damageSource: DamageSource,
    var ignoreDamage: Boolean = false,
) {
    companion object {
        fun of(entity: LivingEntity, damageSource: DamageSource) =
            IgnoreDamageCommand(entity.level(), entity, damageSource)
    }
}