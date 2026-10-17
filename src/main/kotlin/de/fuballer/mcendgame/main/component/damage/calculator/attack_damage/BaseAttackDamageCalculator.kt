package de.fuballer.mcendgame.main.component.damage.calculator.attack_damage

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.calculator.DamageCalculator
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object BaseAttackDamageCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = true

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        return (originalDamage * damageMulti).toFloat()
    }
}