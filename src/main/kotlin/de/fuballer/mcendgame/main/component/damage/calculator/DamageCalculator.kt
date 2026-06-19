package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

interface DamageCalculator {
    fun isActive(source: DamageSource): Boolean

    fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand,
    ): Float

    fun calculateSpellDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand,
    ): Float
}