package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.world.Difficulty
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.ElderGuardian
import net.minecraft.world.entity.monster.Guardian

object GuardianMagicCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is Guardian && source.type().isOf(DamageTypes.INDIRECT_MAGIC)

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        var base = 1f
        if (source.directEntity is ElderGuardian) base += 2
        if (event.world.difficulty == Difficulty.HARD) base += 2
        return base
    }

    override fun calculateElementalDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ) = 0f
}