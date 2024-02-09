package de.fuballer.mcendgame.component.damage.dmg

import de.fuballer.mcendgame.component.damage.DamageCalculation
import de.fuballer.mcendgame.event.DamageCalculationEvent
import org.bukkit.event.entity.EntityDamageEvent

object EntityAttackDamageCalculator : DamageTypeCalculator {
    override val damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK

    override fun getBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageCalculation.rawBaseDamage(event) ?: return 0.0

        damage += DamageCalculation.strengthDamage(event.player)

        if (event.isCritical) {
            damage *= 1.5
        }

        var enchantDamage = event.enchantDamage

        damage *= DamageCalculation.attackCooldownMultiplier(event.player)
        enchantDamage *= DamageCalculation.enchantAttackCooldownMultiplier(event.player)

        damage += enchantDamage

        return damage
    }
}