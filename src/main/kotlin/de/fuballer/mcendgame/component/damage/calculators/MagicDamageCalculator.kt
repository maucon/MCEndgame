package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent

object MagicDamageCalculator : DamageCauseCalculator() {
    override val damageType = EntityDamageEvent.DamageCause.MAGIC
    override val canBeBlocked = false
    override val affectedByInvulnerability = true
    override val affectedByArmor = false
    override val scaledByDifficulty = false
    override val affectedByArmorProtection = true

    override fun getFlatMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val baseReducedDamage = super.getFlatMagicDamageReduction(event, damage)
        if (event.damaged.type != EntityType.WITCH) return baseReducedDamage

        return (damage - baseReducedDamage) * DamageUtil.getWitchMagicDamageReduction()
    }
}