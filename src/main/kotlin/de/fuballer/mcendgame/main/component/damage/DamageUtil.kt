package de.fuballer.mcendgame.main.component.damage

import de.fuballer.mcendgame.main.component.custom_attribute.effects.SpellResistanceSettings
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.CombatRules
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.enchantment.EnchantmentHelper
import kotlin.math.min

object DamageUtil {
    /**
     * Returns the reduced amount
     */
    fun reduceDamageByArmor(
        victim: LivingEntity,
        damageAmount: Float,
        damageSource: DamageSource
    ): Float {
        // there is a vanilla livingEntity.armorValue, but it rounds the armor down - we do not want that
        val armor = victim.getAttributeValue(Attributes.ARMOR).toFloat()
        val armorToughness = victim.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat()

        // this also includes calculating the reduced armor effectiveness (e.g. Breach enchantment)
        return CombatRules.getDamageAfterAbsorb(victim, damageAmount, damageSource, armor, armorToughness)
    }

    /**
     * Returns the reduced amount
     */
    fun reduceDamageByProtectionEnchantment(
        victim: LivingEntity,
        damageAmount: Float,
        damageSource: DamageSource
    ): Float {
        val serverWorld = victim.level() as? ServerLevel ?: return damageAmount
        val protectionAmount = EnchantmentHelper.getDamageProtection(serverWorld, victim, damageSource)
        return CombatRules.getDamageAfterMagicAbsorb(damageAmount, protectionAmount)
    }

    /**
     * Returns the amount of damage resisted
     */
    fun getDamageReductionByResistanceEffect(
        victim: LivingEntity,
        damageAmount: Float
    ): Float {
        val resistance = (victim.getEffect(MobEffects.RESISTANCE)?.amplifier ?: -1) + 1
        val resistancePercent = resistance * 0.2f

        return min(damageAmount * resistancePercent, damageAmount)
    }

    /**
     * Returns the reduced amount
     */
    fun reduceDamageBySpellResistance(
        damageAmount: Float,
        cmd: DamageCalculationCommand,
    ): Float {
        var amount = damageAmount
        val spellResistance = min(SpellResistanceSettings.LIMIT, cmd.spellResistance.sum()).toFloat()
        amount *= 1 - spellResistance

        return amount
    }

    /**
     * Returns the amount of damage resisted
     */
    fun reduceDamageByDamageTakenAttribute(
        damageAmount: Float,
        cmd: DamageCalculationCommand,
    ): Float {
        val increasedDamage = 1 + cmd.increasedDamageTaken.sum()
        val moreDamage = cmd.moreDamageTaken.fold(1.0) { a, b -> a * (1 + b) }

        var totalFactor = increasedDamage * moreDamage
        totalFactor = totalFactor.coerceAtLeast(0.0)

        return (damageAmount * (1 - totalFactor)).toFloat()
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
    ): Double {
        val base = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue ?: 0.0
        return base +
                getAttackDamageModifierValues(livingEntity, AttributeModifier.Operation.ADD_VALUE).sum() +
                event.attackDamage.sum()
    }

    fun getAttackDamageModifierValues(
        livingEntity: LivingEntity,
        operation: AttributeModifier.Operation,
    ) = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE)?.let { instance ->
        instance.modifiers.filter { it.operation == operation }.map { it.amount }
    } ?: listOf()
}