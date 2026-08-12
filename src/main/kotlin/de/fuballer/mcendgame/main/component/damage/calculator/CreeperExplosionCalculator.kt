package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object CreeperExplosionCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = true

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        return (originalDamage * damageMulti).toFloat()
    }
}