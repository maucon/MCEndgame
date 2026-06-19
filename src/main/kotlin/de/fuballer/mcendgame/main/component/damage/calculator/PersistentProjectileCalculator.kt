package de.fuballer.mcendgame.main.component.damage.calculator

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.DamageUtil
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.mixin.PersistentProjectileEntityMixinExtension.getDamage
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.item.enchantment.EnchantmentHelper
import kotlin.math.ceil
import kotlin.random.Random

// skeleton arrows (bogged, stray)
object PersistentProjectileCalculator : DamageCalculator {
    override fun isActive(source: DamageSource) = source.directEntity is AbstractArrow

    override fun calculateAttackDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        val attacker = source.entity as? LivingEntity ?: return originalDamage

        val baseDamage = calculateBaseAttackDamage(source)
        val enchantmentDamage = calculateEnchantmentDamage(attacker, attacked, source)
        val projectileSpeedMulti = calculateOtherMultiplier(source)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)

        val arrowDamage = ceil(projectileSpeedMulti * (baseDamage + enchantmentDamage))
        val criticalDamage = calculateCriticalDamage(event, arrowDamage)
        return ((arrowDamage + criticalDamage) * damageMulti).toFloat()
    }

    override fun calculateSpellDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        event: DamageCalculationCommand
    ): Float {
        if (source.entity !is LivingEntity) return 0.0F

        val baseDamage = calculateBaseSpellDamage(event)
        val damageMulti = DamageUtil.calculateAttackDamageMultiplier(event)
        val projectileSpeedMulti = calculateOtherMultiplier(source)

        val arrowDamage = ceil(projectileSpeedMulti * baseDamage)
        val criticalDamage = if (event.applyCritToSpellDamage) calculateCriticalDamage(event, arrowDamage) else 0
        return ((arrowDamage + criticalDamage) * damageMulti).toFloat()
    }

    private fun calculateBaseAttackDamage(source: DamageSource): Double {
        val persistentProjectile = source.directEntity as AbstractArrow
        return persistentProjectile.getDamage()
    }

    private fun calculateEnchantmentDamage(attacker: LivingEntity, attacked: LivingEntity, source: DamageSource): Double {
        val projectile = source.directEntity as Projectile
        val weaponStack = projectile.weaponItem ?: return 0.0

        return EnchantmentHelper.modifyDamage(attacker.level() as ServerLevel, weaponStack, attacked, source, 0.0F).toDouble()
    }

    private fun calculateCriticalDamage(event: DamageCalculationCommand, amount: Double): Int {
        if (!event.isCritical) return 0
        val critMultiDamage = amount * event.criticalDamageMulti.sum()
        return Random.nextInt(amount.toInt() / 2 + 2) + critMultiDamage.toInt()
    }

    private fun calculateOtherMultiplier(source: DamageSource): Double {
        val sourceEntity = source.directEntity as AbstractArrow
        return sourceEntity.deltaMovement.length()
    }

    private fun calculateBaseSpellDamage(
        event: DamageCalculationCommand
    ): Double {
        return event.spellDamage.sum()
    }
}