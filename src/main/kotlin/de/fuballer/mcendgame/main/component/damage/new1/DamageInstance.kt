package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

data class DamageInstance(
    val damageComponents: MutableMap<DamageCategory, Float> = mutableMapOf(),
) {
    fun setAttackDamage(amount: Float): DamageInstance {
        damageComponents[DamageCategory.ATTACK_DAMAGE] = amount
        return this
    }

    fun setSpellDamage(amount: Float): DamageInstance {
        damageComponents[DamageCategory.SPELL_DAMAGE] = amount
        return this
    }

    fun setTrueDamage(amount: Float): DamageInstance {
        damageComponents[DamageCategory.TRUE_DAMAGE] = amount
        return this
    }

    fun getRawDamage(): Float {
        return damageComponents.values.sum()
    }

    fun getAfterDamageReduction(
        victim: LivingEntity,
        source: DamageSource,
        cmd: DamageCalculationCommand
    ): DamageReductionResult {
        return damageComponents
            .map { (category, damage) -> category.applyDamageReduction(damage, victim, source, cmd) }
            .fold(DamageReductionResult.zero()) { acc, result -> acc + result }
    }

    override fun toString(): String {
        return "DamageInstance(${damageComponents.entries.joinToString { "${it.key}=${it.value}" }})"
    }
}