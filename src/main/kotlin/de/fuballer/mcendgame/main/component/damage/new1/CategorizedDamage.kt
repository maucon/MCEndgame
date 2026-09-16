package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

data class CategorizedDamage(
    private val damageComponents: Map<DamageCategory, Float> = emptyMap()
) {
    constructor(vararg damageComponents: Pair<DamageCategory, Float>) : this(damageComponents.toMap())

    fun transformDamage(transform: (DamageCategory, Float) -> Float) =
        CategorizedDamage(damageComponents.mapValues { (category, amount) -> transform(category, amount) })

    fun getRawDamage() = damageComponents.values.sum()

    fun getAfterDamageReduction(
        victim: LivingEntity,
        source: DamageSource,
        cmd: DamageCalculationCommand
    ): DamageReductionResult =
        damageComponents
            .map { (category, damage) -> category.applyDamageReduction(damage, victim, source, cmd) }
            .fold(DamageReductionResult.zero()) { acc, result -> acc + result }

    override fun toString(): String =
        "CategorizedDamage(${damageComponents.entries.joinToString { "${it.key}=${it.value}" }})"
}