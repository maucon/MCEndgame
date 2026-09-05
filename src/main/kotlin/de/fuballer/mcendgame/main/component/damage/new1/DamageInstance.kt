package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

data class DamageInstance(
    private val damageComponents: MutableMap<DamageCategory, Float> = mutableMapOf(),
) {
    fun setDamage(category: DamageCategory, amount: Float): DamageInstance {
        damageComponents[category] = amount
        return this
    }

    fun transformDamage(transform: (DamageCategory, Float) -> Float) {
        damageComponents.replaceAll(transform)
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

    operator fun plus(other: DamageInstance): DamageInstance {
        val result = DamageInstance(damageComponents.toMutableMap())
        other.damageComponents.forEach { (category, amount) ->
            result.damageComponents[category] = (result.damageComponents[category] ?: 0f) + amount
        }
        return result
    }

    override fun toString(): String {
        return "DamageInstance(${damageComponents.entries.joinToString { "${it.key}=${it.value}" }})"
    }
}