package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile

/**
 * Eggs, Brown Eggs, Blue Eggs
 */
object OtherProjectilesCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is Projectile

    override fun calculateDamage(originalDamage: Float, attacked: LivingEntity, source: DamageSource, event: DamageCalculationCommand): Float {
        return 0f
    }
}