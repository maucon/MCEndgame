package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.getAddedBaseDamage
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.ceil

object ProjectileDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.PROJECTILE

    override fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildDamageEventForPlayer(event) ?: return null

        val projectile = event.damager as Projectile
        val baseDamage = DamageUtil.getProjectileBaseDamage(projectile)
        val addedDamage = projectile.getAddedBaseDamage() ?: 0.0
        val projectileVelocity = projectile.velocity.length()
        val cumulativeBaseDamage = (baseDamage + addedDamage) * projectileVelocity

        damageEvent.baseDamage.add(cumulativeBaseDamage)
        damageEvent.isCritical = DamageUtil.isProjectileCritical(projectile)

        if (damageEvent.isCritical) {
            val eventBaseDamage = event.getDamage(DamageModifier.BASE)
            damageEvent.criticalRoll = eventBaseDamage / (baseDamage * projectileVelocity) - 1
        }

        return damageEvent
    }

    override fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildDamageEventForNonPlayer(event) ?: return null

        val rawDamage = DamageUtil.reverseDifficultyDamage(damageEvent.difficulty, event.damage)
        damageEvent.baseDamage.add(rawDamage)

        return damageEvent
    }

    override fun getNormalBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageUtil.getRawBaseDamage(event) ?: return 0.0

        if (event.isCritical) {
            damage *= 1 + event.criticalRoll
        }

        return ceil(damage - 0.0001) // 🤓🤓🤓
    }

    override fun getMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.getSpecialEnchantDamageReduction(event.damaged, Enchantment.PROTECTION_PROJECTILE)
        return damage * reduction
    }
}