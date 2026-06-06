package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Ghast
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball

object FireballCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is LargeFireball

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        // redirected fireballs deal 1000 damage to ghasts
        if (source.entity is Player && attacked is Ghast) return 1000f
        return 6f
    }

    override fun calculateElementalDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ) = 0f
}