package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object BaseDamageCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = true

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        // TODO damageTypeKey if this should apply
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        return (originalDamage * damageMulti).toFloat()
    }
}