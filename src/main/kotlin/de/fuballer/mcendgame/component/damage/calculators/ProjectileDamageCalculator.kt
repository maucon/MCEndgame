package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.extension.ProjectileExtension.getAddedBaseDamage
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Arrow
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.ceil

private const val ADDED_PROJECTILE_DAMAGE_VELOCITY_SCALING = 0.5

object ProjectileDamageCalculator : DamageCauseCalculator() {
    override val damageType = EntityDamageEvent.DamageCause.PROJECTILE
    override val canBeBlocked = true
    override val affectedByInvulnerability = true
    override val affectedByArmor = true
    override val scaledByDifficulty = true
    override val affectedByArmorProtection = true
    override val specialEnchantDamageReduction: Enchantment = Enchantment.PROTECTION_PROJECTILE

    override fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        val projectile = event.damager as Projectile
        val projectileDamage = DamageUtil.getProjectileBaseDamage(projectile)
        val addedDamage = projectile.getAddedBaseDamage() ?: 0.0

        println("$projectileDamage $addedDamage")
        if (projectile !is Arrow) {
            val cumulativeBaseDamage = projectileDamage + addedDamage
            damageEvent.baseDamage.add(cumulativeBaseDamage)

            return damageEvent
        }

        val projectileVelocity = projectile.velocity.length()
        val baseDamage = projectileDamage * projectileVelocity
        val cumulativeBaseDamage = baseDamage + addedDamage * projectileVelocity * ADDED_PROJECTILE_DAMAGE_VELOCITY_SCALING

        damageEvent.baseDamage.add(cumulativeBaseDamage)
        damageEvent.isCritical = DamageUtil.isProjectileCritical(projectile)

        if (damageEvent.isCritical) {
            val eventBaseDamage = event.getDamage(DamageModifier.BASE)
            damageEvent.criticalRoll = eventBaseDamage / baseDamage - 1
        }

        return damageEvent
    }

    override fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        val rawDamage = DamageUtil.reverseDifficultyDamage(damageEvent.difficulty, event.damage)
        val projectile = event.damager as Projectile
        val addedDamage = projectile.getAddedBaseDamage() ?: 0.0
        val cumulativeBaseDamage = (rawDamage + addedDamage)

        damageEvent.baseDamage.add(cumulativeBaseDamage)

        return damageEvent
    }

    override fun getNormalBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageUtil.getRawBaseDamage(event)

        if (event.isCritical) {
            damage *= 1 + event.criticalRoll
        }

        return ceil(damage - 0.0001) // 🤓🤓🤓
    }
}