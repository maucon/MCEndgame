package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getAttackCooldownMultiplier
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.MaceItem
import net.minecraft.registry.RegistryKeys
import kotlin.math.min

object MaceSmashAttackCalculator : DamageCalculator {
    override fun isActive(source: DamageSource): Boolean {
        val attacker = source.source as? PlayerEntity ?: return false
        return MaceItem.shouldDealAdditionalDamage(attacker)
    }

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val attacker = source.attacker as PlayerEntity

        val baseDamage = DamageUtil.getAttackDamageBaseValue(event, attacker)
        val enchantmentDamage = DamageUtil.calculateEnchantmentDamage(attacker, attacked, source)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        val critMulti = calculateCriticalMultiplier(event)
        val attackCooldown = getAttackCooldown(source)
        val attackDamageMulti = calculateAttackCooldownMulti(attackCooldown)

        val fallDistance = attacker.fallDistance

        val density = attacker.entityWorld.registryManager
            .getOrThrow(RegistryKeys.ENCHANTMENT)
            .getEntry(Enchantments.DENSITY.value).get()

        val densityLevel = EnchantmentHelper.getLevel(density, attacker.mainHandStack)
        val fallBonus = calculateFallBonus(fallDistance, densityLevel)

        return (((baseDamage * attackDamageMulti + fallBonus) * critMulti + enchantmentDamage * attackCooldown) * damageMulti).toFloat()
    }

    override fun calculateElementalDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val baseDamage = calculateBaseElementalDamage(event)
        val damageMulti = DamageUtil.calculateElementalDamageMultiplier(event)
        val critMulti = if (event.applyCritToElementalDamage) calculateCriticalMultiplier(event) else 1.0
        val attackCooldown = getAttackCooldown(source)
        val attackDamageMulti = calculateAttackCooldownMulti(attackCooldown)

        return (baseDamage * attackDamageMulti * critMulti * damageMulti).toFloat()
    }

    private fun calculateFallBonus(fallDistance: Double, densityLevel: Int): Double {
        val tier1 = min(fallDistance, 3.0) * 4.0
        val tier2 = (min(fallDistance, 8.0) - min(fallDistance, 3.0)) * 2.0
        val tier3 = (fallDistance - min(fallDistance, 8.0)).coerceAtLeast(0.0) * 1.0

        val densityBonus = densityLevel * 0.5 * fallDistance

        return tier1 + tier2 + tier3 + densityBonus
    }

    private fun calculateCriticalMultiplier(event: DamageCalculationCommand): Double {
        if (!event.isCritical) return 1.0
        return 1.5 + event.criticalDamageMulti.sum()
    }

    private fun getAttackCooldown(source: DamageSource): Double {
        val sourceEntity = source.source as? PlayerEntity ?: return 1.0
        return sourceEntity.getAttackCooldownMultiplier().toDouble()
    }

    private fun calculateAttackCooldownMulti(attackCooldown: Double): Double {
        return 0.2 + attackCooldown * attackCooldown * 0.8
    }

    private fun calculateBaseElementalDamage(
        event: DamageCalculationCommand
    ): Double {
        return event.elementalDamage.sum()
    }
}