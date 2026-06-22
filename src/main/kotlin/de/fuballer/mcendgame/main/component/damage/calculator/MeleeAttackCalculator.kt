package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getAttackCooldownMultiplier
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player

object MeleeAttackCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is LivingEntity

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val attacker = source.entity as? LivingEntity ?: return originalDamage

        val baseDamage = calculateBaseAttackDamage(event, attacker, source)
        val enchantmentDamage = DamageUtil.calculateEnchantmentDamage(attacker, attacked, source)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        val critMulti = calculateCriticalMultiplier(event)
        val attackCooldown = getAttackCooldown(source)
        val attackDamageMulti = calculateAttackCooldownMulti(attackCooldown)

        return ((baseDamage * attackDamageMulti * critMulti + enchantmentDamage * attackCooldown) * damageMulti).toFloat()
    }

    override fun calculateSpellDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float = 0.0F

    private fun calculateBaseAttackDamage(
        event: DamageCalculationCommand,
        attacker: LivingEntity,
        source: DamageSource
    ): Double {
        var baseDamage = if (attacker.isAutoSpinAttack) 8.0
        else DamageUtil.getAttackDamageBaseValue(event, attacker)

        if (source.type().isOf(CustomDamageTypes.SWEEPING)) {
            val sweepingRatio = attacker.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO)
            baseDamage = 1.0 + sweepingRatio * baseDamage
        }

        return baseDamage
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