package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.Difficulty
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent

object MagicDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.MAGIC

    override fun getBaseDamage(event: DamageCalculationEvent): Double {
        val normalBaseDamage = getNormalBaseDamage(event)
        return scaleInvulnerabilityDamage(event.damaged, normalBaseDamage)
    }

    override fun scaleDifficultyDamage(difficulty: Difficulty, damage: Double) = damage
    override fun getFlatBlockingDamageReduction(event: DamageCalculationEvent, damage: Double) = 0.0
    override fun getFlatArmorDamageReduction(event: DamageCalculationEvent, damage: Double) = 0.0

    override fun getFlatMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val protectionReducedDamage = damage * DamageUtil.getProtectionDamageReduction(event.damaged)
        if (event.damaged.type != EntityType.WITCH) return protectionReducedDamage

        return (damage - protectionReducedDamage) * DamageUtil.getWitchMagicDamageReduction()
    }
}