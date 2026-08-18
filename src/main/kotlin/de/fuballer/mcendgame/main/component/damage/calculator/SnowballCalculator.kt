package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Blaze
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball

object SnowballCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is Snowball

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        if (attacked is Blaze) return 3.0f
        return 0.0f
    }
}