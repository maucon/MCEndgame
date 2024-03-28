package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object SonicBoomDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.SONIC_BOOM

    override fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        val rawDamage = DamageUtil.reverseDifficultyDamage(damageEvent.difficulty, event.damage)
        damageEvent.baseDamage.add(rawDamage)
        return damageEvent
    }

    override fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double) = damage

    override fun getFlatBlockingDamageReduction(event: DamageCalculationEvent, damage: Double) = 0.0
    override fun getFlatMagicDamageReduction(event: DamageCalculationEvent, damage: Double) = 0.0
    override fun getFlatArmorDamageReduction(event: DamageCalculationEvent, damage: Double) = 0.0
}