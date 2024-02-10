package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import org.bukkit.Difficulty
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object ThornsDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.THORNS

    override fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildDamageEventForPlayer(event) ?: return null
        damageEvent.baseDamage.add(event.damage)
        return damageEvent
    }

    override fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double) = damage
    override fun scaleDifficultyDamage(difficulty: Difficulty, damage: Double) = damage
}