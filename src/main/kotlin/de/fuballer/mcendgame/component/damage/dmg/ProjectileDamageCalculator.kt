package de.fuballer.mcendgame.component.damage.dmg

import de.fuballer.mcendgame.component.damage.DamageCalculation
import de.fuballer.mcendgame.event.DamageCalculationEvent
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.ceil
import kotlin.random.Random

object ProjectileDamageCalculator : DamageTypeCalculator {
    override val damageType = EntityDamageEvent.DamageCause.PROJECTILE

    override fun getBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageCalculation.rawBaseDamage(event) ?: return 0.0

        if (event.isCritical) {
            damage += Random.nextDouble() * (0.5 * damage + 2) // TODO crit roll should calculated beforehand
        }

        return ceil(damage)
    }

    override fun getMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageCalculation.specialEnchantDamageReduction(event.damaged, Enchantment.PROTECTION_PROJECTILE)
        return damage * reduction
    }
}