package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball

object SmallFireballCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is SmallFireball

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        // not really necessary, as entities with fire res just block small fireballs
        // but just to be safe i guess
        if (attacked.hasEffect(MobEffects.FIRE_RESISTANCE)) return 0.0F
        return 5f
    }
}