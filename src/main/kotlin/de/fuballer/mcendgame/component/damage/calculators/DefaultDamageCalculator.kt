package de.fuballer.mcendgame.component.damage.calculators

import org.bukkit.event.entity.EntityDamageEvent

object DefaultDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.CUSTOM
}