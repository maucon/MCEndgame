package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import kotlin.random.Random

object ThornsCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.type().isOf(DamageTypes.THORNS)

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        val baseDamage = Random.nextFloat() * 4 + 1 // 1 to 5 damage
        val damageMulti = DamageUtil.calculateGenericDamageMultiplier(event)
        return (baseDamage * damageMulti).toFloat()
    }
}