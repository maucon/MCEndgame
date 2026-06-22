package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.arrow.ThrownTrident

// TODO drowned with trident
object TridentProjectileCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is ThrownTrident

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val attacker = source.entity as? LivingEntity ?: return originalDamage

        val baseDamage = 8.0
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        val enchantmentDamage = DamageUtil.calculateEnchantmentDamage(attacker, attacked, source)

        return ((baseDamage + enchantmentDamage) * damageMulti).toFloat()
    }

    override fun calculateSpellDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ) = 0.0f
}