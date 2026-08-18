package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource

sealed class DamageSourceResult(damageSource: DamageSource) : DamageSource(
    damageSource.typeHolder(),
    damageSource.directEntity,
    damageSource.entity
) {
    val isDamageApplying: Boolean
        get() = this !is NoDamage

    open fun getRawDamage(): Float = 0f

    class Applied(
        val damageInstance: DamageInstance,
        val damageCalculationCommand: DamageCalculationCommand,
        damageSource: DamageSource
    ) : DamageSourceResult(damageSource) {
        override fun getRawDamage() = damageInstance.getRawDamage()
    }

    class ZeroDamage(
        val damageCalculationCommand: DamageCalculationCommand,
        damageSource: DamageSource
    ) : DamageSourceResult(damageSource)

    class NoDamage(
        damageSource: DamageSource
    ) : DamageSourceResult(damageSource)
}