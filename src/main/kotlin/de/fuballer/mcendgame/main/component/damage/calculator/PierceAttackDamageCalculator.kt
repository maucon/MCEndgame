package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object PierceAttackDamageCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.type().isOf(CustomDamageTypes.PIERCE_ATTACK)

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val attacker = source.entity as? LivingEntity ?: return originalDamage

        val baseDamage = DamageUtil.getAttackDamageBaseValue(event, attacker)
        val enchantmentDamage = DamageUtil.calculateEnchantmentDamage(attacker, attacked, source)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)

        return ((baseDamage + enchantmentDamage) * damageMulti).toFloat()
    }

    override fun calculateSpellDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        if (source.entity !is LivingEntity) return 0.0F

        val baseDamage = event.spellDamage.sum()
        val damageMulti = DamageUtil.calculateSpellDamageMultiplier(event)

        return (baseDamage * damageMulti).toFloat()
    }
}