package de.fuballer.mcendgame.component.damage.dmg

import de.fuballer.mcendgame.component.damage.DamageCalculation
import de.fuballer.mcendgame.event.DamageCalculationEvent
import org.bukkit.event.entity.EntityDamageEvent

object EntitySweepDamageCalculator : DamageTypeCalculator {
    override val damageType = EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK

    override fun getBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageCalculation.rawBaseDamage(event) ?: return 0.0

        damage += DamageCalculation.strengthDamage(event.player)

        if (event.isCritical) {
            damage *= 1.5
        }

        damage += event.enchantDamage
        damage = 1 + damage * event.sweepingEdgeMultiplier

        return damage
    }
}