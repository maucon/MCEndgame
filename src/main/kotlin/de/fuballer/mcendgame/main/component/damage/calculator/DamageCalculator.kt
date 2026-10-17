package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

interface DamageCalculator {
    fun isActive(source: DamageSource): Boolean

    fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand,
    ): Float
}