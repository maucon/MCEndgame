package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity

enum class DamageCategory {
    ATTACK_DAMAGE {
        override fun applyDamageReduction(
            damage: Float,
            attacked: LivingEntity,
            source: DamageSource,
            cmd: DamageCalculationCommand
        ): DamageReductionResult {
            var amount = damage
            var damageResisted = 0f

            if (!source.`is`(DamageTypeTags.BYPASSES_ARMOR)) {
                amount = DamageUtil.reduceDamageByArmor(attacked, damage, source)
            }
            if (!source.`is`(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
                amount = DamageUtil.reduceDamageByProtectionEnchantment(attacked, damage, source)
            }
            if (!source.`is`(DamageTypeTags.BYPASSES_EFFECTS)) {
                if (attacked.hasEffect(MobEffects.RESISTANCE) && !source.`is`(DamageTypeTags.BYPASSES_RESISTANCE)) {
                    damageResisted = DamageUtil.getDamageReductionByResistanceEffect(attacked, damage)
                    amount -= damageResisted
                }
            }
            amount -= DamageUtil.reduceDamageByDamageTakenAttribute(damage, cmd)

            return DamageReductionResult(amount, damageResisted)
        }
    },
    SPELL_DAMAGE {
        override fun applyDamageReduction(
            damage: Float,
            attacked: LivingEntity,
            source: DamageSource,
            cmd: DamageCalculationCommand
        ): DamageReductionResult {
            var damageResisted = 0f

            var amount = DamageUtil.reduceDamageBySpellResistance(damage, cmd)
            if (!source.`is`(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
                amount = DamageUtil.reduceDamageByProtectionEnchantment(attacked, damage, source)
            }
            if (!source.`is`(DamageTypeTags.BYPASSES_EFFECTS)) {
                if (attacked.hasEffect(MobEffects.RESISTANCE) && !source.`is`(DamageTypeTags.BYPASSES_RESISTANCE)) {
                    damageResisted = DamageUtil.getDamageReductionByResistanceEffect(attacked, damage)
                    amount -= damageResisted
                }
            }
            amount -= DamageUtil.reduceDamageByDamageTakenAttribute(damage, cmd)

            return DamageReductionResult(amount, damageResisted)
        }
    },
    TRUE_DAMAGE {
        override fun applyDamageReduction(
            damage: Float,
            attacked: LivingEntity,
            source: DamageSource,
            cmd: DamageCalculationCommand
        ) = DamageReductionResult(damage, resistedDamage = 0f)
    };

    abstract fun applyDamageReduction(damage: Float, attacked: LivingEntity, source: DamageSource, cmd: DamageCalculationCommand): DamageReductionResult
}