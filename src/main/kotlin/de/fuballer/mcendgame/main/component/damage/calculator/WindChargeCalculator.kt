package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge

object WindChargeCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is AbstractWindCharge

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        return 1f
    }
}