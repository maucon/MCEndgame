package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getAttackCooldownMultiplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.MaceItem
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import kotlin.math.min

object MaceSmashAttackCalculator : DamageCalculator {
    override fun isActive(source: DamageSource): Boolean {
        val attacker = source.directEntity as? Player ?: return false
        if (attacker.mainHandItem.item !is MaceItem) return false
        return MaceItem.canSmashAttack(attacker)
    }

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        val attacker = source.entity as? Player ?: return originalDamage

        val baseDamage = DamageUtil.getAttackDamageBaseValue(event, attacker)
        val enchantmentDamage = DamageUtil.calculateEnchantmentDamage(attacker, attacked, source)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        val critMulti = calculateCriticalMultiplier(event)
        val attackCooldown = getAttackCooldown(source)
        val attackDamageMulti = calculateAttackCooldownMulti(attackCooldown)

        val fallDistance = attacker.fallDistance

        val densityLevel = attacker.level().registryAccess()
            .lookupOrThrow(Registries.ENCHANTMENT)
            .get(Enchantments.DENSITY.identifier())
            .map { EnchantmentHelper.getItemEnchantmentLevel(it, attacker.mainHandItem) }
            .orElse(0)!!

        val fallBonus = calculateFallBonus(fallDistance, densityLevel)

        return (((baseDamage * attackDamageMulti + fallBonus) * critMulti + enchantmentDamage * attackCooldown) * damageMulti).toFloat()
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
        val sourceEntity = source.directEntity as? Player ?: return 1.0
        return sourceEntity.getAttackCooldownMultiplier().toDouble()
    }

    private fun calculateAttackCooldownMulti(attackCooldown: Double): Double {
        return 0.2 + attackCooldown * attackCooldown * 0.8
    }
}