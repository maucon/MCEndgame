package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object GenericAttackCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.type().isOf(CustomDamageTypes.GENERIC_ATTACK) || source.type().isOf(CustomDamageTypes.GENERIC_ATTACK_UNBLOCKABLE)

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        println(originalDamage)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        return (originalDamage * damageMulti).toFloat()
    }
}