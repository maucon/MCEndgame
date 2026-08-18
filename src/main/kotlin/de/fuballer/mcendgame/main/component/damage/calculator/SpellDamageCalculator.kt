package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object SpellDamageCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.type().isOf(CustomDamageTypes.SPELL)

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        val baseDamage = calculateBaseSpellDamage(event)
        val damageMulti = DamageUtil.calculateSpellDamageMultiplier(event)

        val critMulti = calculateCriticalMultiplier(event)
        return (baseDamage * critMulti * damageMulti).toFloat()
    }

    private fun calculateBaseSpellDamage(
        event: DamageCalculationCommand
    ): Double {
        return event.spellDamage.sum()
    }

    private fun calculateCriticalMultiplier(event: DamageCalculationCommand): Double {
        if (!event.isCritical || !event.applyCritToSpellDamage) return 1.0
        return 1.5 + event.criticalDamageMulti.sum()
    }
}