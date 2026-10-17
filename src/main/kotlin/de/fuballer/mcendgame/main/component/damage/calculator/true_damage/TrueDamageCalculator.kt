package de.fuballer.mcendgame.main.component.damage.calculator.true_damage

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.calculator.DamageCalculator
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object TrueDamageCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = true

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        val damageMulti = DamageUtil.calculateGenericDamageMultiplier(event)
        return (originalDamage * damageMulti).toFloat()
    }
}