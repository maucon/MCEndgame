package de.fuballer.mcendgame.main.component.damage

import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.enchantment.EnchantmentHelper

object DamageUtil {
    fun reduceAttackDamageByArmor(
        armorWearer: LivingEntity,
        damageAmount: Float,
        damageSource: DamageSource,
        armor: Float,
        armorToughness: Float
    ): Float {
        val armorReductionReduction = 2.0f + armorToughness / 4.0f
        val effectiveArmor = Mth.clamp(armor - damageAmount / armorReductionReduction, armor * 0.2f, 20.0f)
        var damageReduction = effectiveArmor / 25.0f
        val itemStack = damageSource.weaponItem
        if (itemStack != null && armorWearer.level() is ServerLevel) {
            damageReduction =
                Mth.clamp(EnchantmentHelper.modifyArmorEffectiveness(armorWearer.level() as ServerLevel, itemStack, armorWearer, damageSource, damageReduction), 0.0f, 1.0f)
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

    fun reduceSpellDamageByWard(
        armorWearer: LivingEntity,
        damageAmount: Float,
        damageSource: DamageSource,
        ward: Float,
    ): Float {
        val wardReduction = 0.33F * damageAmount
        val effectiveWard = Mth.clamp(ward - wardReduction, ward / 5.0F, 10F)
        var damageReduction = effectiveWard / 12.5F

        val itemStack = damageSource.weaponItem
        if (itemStack != null && armorWearer.level() is ServerLevel) {
            damageReduction =
                Mth.clamp(EnchantmentHelper.modifyArmorEffectiveness(armorWearer.level() as ServerLevel, itemStack, armorWearer, damageSource, damageReduction), 0.0f, 1.0f)
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
            damageIncrease += getAttackDamageModifierValues(attacker, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).sum()
            moreDamage *= getAttackDamageModifierValues(attacker, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).fold(1.0) { a, b -> a * (b + 1) }
        }

        return damageIncrease * moreDamage
    }

    fun calculateSpellDamageMultiplier(
        event: DamageCalculationCommand
    ): Double {
        var damageIncrease = 1 + event.increasedDamage.sum()
        damageIncrease += event.increasedSpellDamage.sum()

        var moreDamage = event.moreDamage.fold(1.0) { a, b -> a * (b + 1) }
        moreDamage *= event.moreSpellDamage.fold(1.0) { a, b -> a * (b + 1) }

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
        return EnchantmentHelper.modifyDamage(attacker.level() as ServerLevel, attacker.weaponItem, attacked, source, 0.0F).toDouble()
    }

    fun getAttackDamageBaseValue(
        event: DamageCalculationCommand,
        livingEntity: LivingEntity,
    ) = livingEntity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE) +
            getAttackDamageModifierValues(livingEntity, AttributeModifier.Operation.ADD_VALUE).sum() +
            event.attackDamage.sum()

    fun getAttackDamageModifierValues(
        livingEntity: LivingEntity,
        operation: AttributeModifier.Operation,
    ) = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE)?.let { instance ->
        instance.modifiers.filter { it.operation == operation }.map { it.amount }
    } ?: listOf()
}