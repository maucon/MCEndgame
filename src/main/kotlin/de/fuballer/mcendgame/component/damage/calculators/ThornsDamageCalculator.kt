package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object ThornsDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.THORNS

    override fun buildDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildDamageEvent(event) ?: return null
        damageEvent.baseDamage.add(event.damage)
        return damageEvent
    }

    override fun getInvulnerabilityDamage(entity: LivingEntity, damage: Double) = damage
}