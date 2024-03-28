package de.fuballer.mcendgame.component.damage.calculators

import org.bukkit.event.entity.EntityDamageEvent

object SonicBoomDamageCalculator : DamageCauseCalculator() {
    override val damageType = EntityDamageEvent.DamageCause.SONIC_BOOM
    override val canBeBlocked = false
    override val affectedByInvulnerability = false
    override val affectedByArmor = false
    override val scaledByDifficulty = true
    override val affectedByArmorProtection = false
}