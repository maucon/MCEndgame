package de.fuballer.mcendgame.component.damage.dmg

import org.bukkit.event.entity.EntityDamageEvent

object DefaultDamageCalculator : DamageTypeCalculator {
    override val damageType = EntityDamageEvent.DamageCause.CUSTOM
}