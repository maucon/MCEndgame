package de.fuballer.mcendgame.main.component.damage.calculator.attack_damage

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.calculator.DamageCalculator
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion

object PotionCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.type().isOf(DamageTypes.INDIRECT_MAGIC)
            && (source.directEntity is AbstractThrownPotion || source.directEntity is AreaEffectCloud)

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        event: DamageCalculationCommand
    ): Float {
        val damageMulti = DamageUtil.calculateGenericDamageMultiplier(event)
        return (originalDamage * damageMulti).toFloat()
    }
}