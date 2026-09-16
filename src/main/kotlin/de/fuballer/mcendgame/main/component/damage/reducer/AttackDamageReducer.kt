package de.fuballer.mcendgame.main.component.damage.reducer

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.new1.DamageReductionResult
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Witch

object AttackDamageReducer : DamageReducer {
    override fun apply(damage: Float, attacked: LivingEntity, source: DamageSource, cmd: DamageCalculationCommand): DamageReductionResult {
        var amount = damage
        var damageResisted = 0f

        if (!source.`is`(DamageTypeTags.BYPASSES_ARMOR)) {
            amount = DamageUtil.reduceDamageByArmor(attacked, amount, source)
        }
        if (!source.`is`(DamageTypeTags.BYPASSES_EFFECTS)) {
            if (attacked.hasEffect(MobEffects.RESISTANCE) && !source.`is`(DamageTypeTags.BYPASSES_RESISTANCE)) {
                damageResisted = DamageUtil.getDamageReductionByResistanceEffect(attacked, amount)
                amount -= damageResisted
            }
            if (!source.`is`(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
                amount = DamageUtil.reduceDamageByProtectionEnchantment(attacked, amount, source)
            }
        }

        // region Taken from Witch::getDamageAfterMagicAbsorb
        if (attacked is Witch) {
            if (source.entity == attacked) amount = 0f
            if (source.`is`(DamageTypeTags.WITCH_RESISTANT_TO)) amount *= 0.15f
        }
        // endregion

        amount -= DamageUtil.reduceDamageByDamageTakenAttribute(amount, cmd)
        return DamageReductionResult(amount.coerceAtLeast(0f), damageResisted)
    }
}