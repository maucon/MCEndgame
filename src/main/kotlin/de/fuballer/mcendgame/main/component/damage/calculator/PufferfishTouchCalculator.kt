package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.fish.Pufferfish

object PufferfishTouchCalculator : DamageCalculator {
    override fun isActive(source: DamageSource): Boolean {
        if (source.entity !is Pufferfish) return false
        return source.type().isOf(DamageTypes.MOB_ATTACK)
    }

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        val entity = source.directEntity as Pufferfish
        return 1F + entity.puffState
    }
}