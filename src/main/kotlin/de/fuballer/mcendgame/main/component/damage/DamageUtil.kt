package de.fuballer.mcendgame.main.component.damage

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.MathHelper

object DamageUtil {
    fun reduceAttackDamageByArmor(
        armorWearer: LivingEntity,
        damageAmount: Float,
        damageSource: DamageSource,
        armor: Float,
        armorToughness: Float
    ): Float {
        val armorReductionReduction = 2.0f + armorToughness / 4.0f
        val effectiveArmor = MathHelper.clamp(armor - damageAmount / armorReductionReduction, armor * 0.2f, 20.0f)
        var damageReduction = effectiveArmor / 25.0f
        val itemStack = damageSource.weaponStack
        if (itemStack != null && armorWearer.entityWorld is ServerWorld) {
            damageReduction =
                MathHelper.clamp(EnchantmentHelper.getArmorEffectiveness(armorWearer.entityWorld as ServerWorld, itemStack, armorWearer, damageSource, damageReduction), 0.0f, 1.0f)
        }

        val damageMultiplier = 1.0f - damageReduction
        return damageAmount * damageMultiplier
    }

    fun applyDamageTakenAttributes(
        damageAmount: Float,
        cmd: DamageCalculationCommand,
    ): Float {
        val increasedDamage = 1 + cmd.increasedDamageTaken.sum()
        val moreDamage = cmd.moreDamageTaken.fold(1.0) { a, b -> a * (1 + b) }

        var totalFactor = increasedDamage * moreDamage
        totalFactor = totalFactor.coerceAtLeast(0.0)

        return (damageAmount * totalFactor).toFloat()
    }

    fun reduceElementalDamageByWard(
        armorWearer: LivingEntity,
        damageAmount: Float,
        damageSource: DamageSource,
        ward: Float,
    ): Float {
        val wardReduction = 0.33F * damageAmount
        val effectiveWard = MathHelper.clamp(ward - wardReduction, ward / 5.0F, 10F)
        var damageReduction = effectiveWard / 12.5F

        val itemStack = damageSource.weaponStack
        if (itemStack != null && armorWearer.entityWorld is ServerWorld) {
            damageReduction =
                MathHelper.clamp(EnchantmentHelper.getArmorEffectiveness(armorWearer.entityWorld as ServerWorld, itemStack, armorWearer, damageSource, damageReduction), 0.0f, 1.0f)
        }

        val damageMultiplier = 1.0f - damageReduction
        return damageAmount * damageMultiplier
    }

    fun calculateAttackDamageMultiplier(
        event: DamageCalculationCommand
    ): Double {
        var damageIncrease = 1 + event.increasedDamage.sum()
        damageIncrease += event.increasedAttackDamage.sum()

        var moreDamage = event.moreDamage.fold(1.0) { a, b -> a * (b + 1) }
        moreDamage *= event.moreAttackDamage.fold(1.0) { a, b -> a * (b + 1) }

        (event.damager as? LivingEntity)?.let { attacker ->
            damageIncrease += getAttackDamageModifierValues(attacker, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE).sum()
            moreDamage *= getAttackDamageModifierValues(attacker, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).fold(1.0) { a, b -> a * (b + 1) }
        }

        return damageIncrease * moreDamage
    }

    fun calculateElementalDamageMultiplier(
        event: DamageCalculationCommand
    ): Double {
        var damageIncrease = 1 + event.increasedDamage.sum()
        damageIncrease += event.increasedElementalDamage.sum()

        var moreDamage = event.moreDamage.fold(1.0) { a, b -> a * (b + 1) }
        moreDamage *= event.moreElementalDamage.fold(1.0) { a, b -> a * (b + 1) }

        return damageIncrease * moreDamage
    }

    fun calculateGenericDamageMultiplier(
        event: DamageCalculationCommand
    ): Double {
        val damageIncrease = 1 + event.increasedDamage.sum()
        val moreDamage = event.moreDamage.fold(1.0) { a, b -> a * (b + 1) }
        return damageIncrease * moreDamage
    }

    fun calculateEnchantmentDamage(
        attacker: LivingEntity,
        attacked: LivingEntity,
        source: DamageSource
    ): Double {
        return EnchantmentHelper.getDamage(attacker.entityWorld as ServerWorld, attacker.weaponStack, attacked, source, 0.0F).toDouble()
    }

    fun getAttackDamageBaseValue(
        event: DamageCalculationCommand,
        livingEntity: LivingEntity,
    ) = livingEntity.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE) +
            getAttackDamageModifierValues(livingEntity, EntityAttributeModifier.Operation.ADD_VALUE).sum() +
            event.attackDamage.sum()

    fun getAttackDamageModifierValues(
        livingEntity: LivingEntity,
        operation: EntityAttributeModifier.Operation,
    ) = livingEntity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)?.let { instance ->
        instance.modifiers.filter { it.operation == operation }.map { it.value }
    } ?: listOf()
}