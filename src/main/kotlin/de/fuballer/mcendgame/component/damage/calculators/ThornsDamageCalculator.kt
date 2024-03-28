package de.fuballer.mcendgame.component.damage.calculators

import org.bukkit.event.entity.EntityDamageEvent.DamageCause

object ThornsDamageCalculator : DamageCauseCalculator() {
    override val damageType = DamageCause.THORNS
    override val canBeBlocked = false
    override val affectedByInvulnerability = false
    override val affectedByArmor = true
    override val scaledByDifficulty = false
    override val affectedByArmorProtection = true
}