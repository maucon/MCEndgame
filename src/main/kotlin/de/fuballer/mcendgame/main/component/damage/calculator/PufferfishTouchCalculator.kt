package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.fish.Pufferfish

object PufferfishTouchCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is Pufferfish

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val entity = source.directEntity as Pufferfish
        return 1F + entity.puffState
    }
}