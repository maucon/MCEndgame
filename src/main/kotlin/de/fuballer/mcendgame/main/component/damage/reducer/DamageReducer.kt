package de.fuballer.mcendgame.main.component.damage.reducer

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.new1.DamageReductionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

interface DamageReducer {
    fun apply(damage: Float, attacked: LivingEntity, source: DamageSource, cmd: DamageCalculationCommand): DamageReductionResult
}