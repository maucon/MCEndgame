package de.fuballer.mcendgame.main.component.damage.reducer

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.new1.DamageReductionResult
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity

object SpellDamageReducer : DamageReducer {
    override fun apply(damage: Float, attacked: LivingEntity, source: DamageSource, cmd: DamageCalculationCommand): DamageReductionResult {
        var damageResisted = 0f
        var amount = DamageUtil.reduceDamageBySpellResistance(damage, cmd)

        if (!source.`is`(DamageTypeTags.BYPASSES_EFFECTS)) {
            if (attacked.hasEffect(MobEffects.RESISTANCE) && !source.`is`(DamageTypeTags.BYPASSES_RESISTANCE)) {
                damageResisted = DamageUtil.getDamageReductionByResistanceEffect(attacked, amount)
                amount -= damageResisted
            }
            if (!source.`is`(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
                amount = DamageUtil.reduceDamageByProtectionEnchantment(attacked, amount, source)
            }
        }
        amount -= DamageUtil.reduceDamageByDamageTakenAttribute(amount, cmd)
        return DamageReductionResult(amount.coerceAtLeast(0f), damageResisted)
    }
}