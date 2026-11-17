package de.fuballer.mcendgame.main.component.damage.calculator.attack_damage

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.calculator.DamageCalculator
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import net.minecraft.core.component.DataComponents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.component.KineticWeapon
import kotlin.math.floor
import kotlin.math.max

object KineticAttackDamageCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.type().isOf(CustomDamageTypes.KINETIC_ATTACK)

    override fun calculateDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        cmd: DamageCalculationCommand
    ): Float {
        val attacker = source.entity as? LivingEntity ?: return originalDamage

        val baseDamage = DamageUtil.getAttackDamageBaseValue(cmd, attacker)
        val enchantmentDamage = DamageUtil.calculateEnchantmentDamage(attacker, attacked, source)
        val kineticWeapon = attacker.weaponItem.get(DataComponents.KINETIC_WEAPON) ?: return originalDamage
        val damageMultiplier = kineticWeapon.damageMultiplier()

        val attackerRotation = attacker.lookAngle
        val attackerMovement = attackerRotation.dot(KineticWeapon.getMotion(attacker))
        val attackedMovement = attackerRotation.dot(KineticWeapon.getMotion(attacked))
        val relativeMovement = max(0.0, attackerMovement - attackedMovement)

        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(cmd)

        return ((baseDamage + enchantmentDamage + floor(damageMultiplier * relativeMovement)) * damageMulti).toFloat()
    }
}