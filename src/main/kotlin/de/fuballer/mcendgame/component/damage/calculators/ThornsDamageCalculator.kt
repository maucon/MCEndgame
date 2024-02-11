package de.fuballer.mcendgame.component.damage.calculators

import org.bukkit.Difficulty
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

object ThornsDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.THORNS

    override fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double) = damage
    override fun scaleDifficultyDamage(difficulty: Difficulty, damage: Double) = damage
}