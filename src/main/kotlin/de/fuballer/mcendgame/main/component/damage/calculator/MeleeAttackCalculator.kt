package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getAttackCooldownMultiplier
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity

object MeleeAttackCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.source is LivingEntity

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val attacker = source.attacker as? LivingEntity ?: return originalDamage

        val baseDamage = calculateBaseAttackDamage(event, attacker, source)
        val enchantmentDamage = DamageUtil.calculateEnchantmentDamage(attacker, attacked, source)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        val critMulti = calculateCriticalMultiplier(event)
        val attackCooldown = getAttackCooldown(source)
        val attackDamageMulti = calculateAttackCooldownMulti(attackCooldown)

        return ((baseDamage * attackDamageMulti * critMulti + enchantmentDamage * attackCooldown) * damageMulti).toFloat()
    }

    override fun calculateElementalDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        if (source.attacker !is LivingEntity) return 0.0F

        val baseDamage = calculateBaseElementalDamage(event)
        val damageMulti = DamageUtil.calculateElementalDamageMultiplier(event)
        val critMulti = if (event.applyCritToElementalDamage) calculateCriticalMultiplier(event) else 1.0
        val attackCooldown = getAttackCooldown(source)
        val attackDamageMulti = calculateAttackCooldownMulti(attackCooldown)

        return (baseDamage * attackDamageMulti * critMulti * damageMulti).toFloat()
    }

    private fun calculateBaseAttackDamage(
        event: DamageCalculationCommand,
        attacker: LivingEntity,
        source: DamageSource
    ): Double {
        var baseDamage = if (attacker.isUsingRiptide) 8.0
        else DamageUtil.getAttackDamageBaseValue(event, attacker)

        if (source.type.isOf(CustomDamageTypes.SWEEPING)) {
            val sweepingRatio = attacker.getAttributeValue(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO)
            baseDamage = 1.0 + sweepingRatio * baseDamage
        }

        return baseDamage
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