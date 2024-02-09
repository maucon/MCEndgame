package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.ceil

object ProjectileDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.PROJECTILE

    override fun buildDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildDamageEvent(event) ?: return null

        val projectile = event.damager as Projectile
        val baseDamage = DamageUtil.projectileBaseDamage(projectile, event)

        damageEvent.baseDamage.add(baseDamage)
        damageEvent.isCritical = DamageUtil.isProjectileCritical(projectile)
        damageEvent.criticalRoll = 0.2

        return damageEvent
    }

    override fun getBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageUtil.rawBaseDamage(event) ?: return 0.0

        if (event.isCritical) {
            damage *= 1 + event.criticalRoll
        }

        return ceil(damage)
    }

    override fun getMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.specialEnchantDamageReduction(event.damaged, Enchantment.PROTECTION_PROJECTILE)
        return damage * reduction
    }
}