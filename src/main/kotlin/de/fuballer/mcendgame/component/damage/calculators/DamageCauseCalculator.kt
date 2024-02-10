package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Difficulty
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier

interface DamageCauseCalculator {
    val damageType: DamageCause

    fun buildDamageEvent(event: EntityDamageByEntityEvent) =
        if (event.damager is Player) buildDamageEventForPlayer(event)
        else buildDamageEventForNonPlayer(event)

    fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent) = buildBaseDamageEvent(event)

    fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent) = buildBaseDamageEvent(event)

    fun getBaseDamage(event: DamageCalculationEvent) =
        if (event.damager is Player) getBaseDamageForPlayer(event)
        else getBaseDamageForNonPlayer(event)

    fun getNormalBaseDamage(event: DamageCalculationEvent) = DamageUtil.getRawBaseDamage(event) ?: 0.0

    fun getBaseDamageForPlayer(event: DamageCalculationEvent): Double {
        val normalBaseDamage = getNormalBaseDamage(event)
        return scaleInvulnerabilityDamage(event.damaged, normalBaseDamage)
    }

    fun getBaseDamageForNonPlayer(event: DamageCalculationEvent): Double {
        val normalBaseDamage = getNormalBaseDamage(event)
        val baseDamage = scaleDifficultyDamage(event.difficulty, normalBaseDamage)
        return scaleInvulnerabilityDamage(event.damaged, baseDamage)
    }

    fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double) = DamageUtil.scaleInvulnerabilityDamage(entity, damage)

    fun scaleDifficultyDamage(difficulty: Difficulty, damage: Double) = DamageUtil.scaleDifficultyDamage(difficulty, damage)

    fun getDamageReduction(event: DamageCalculationEvent, damage: Double, modifier: DamageModifier) =
        when (modifier) {
            DamageModifier.BASE -> throw IllegalArgumentException("$modifier is not a damage reduction")
            DamageModifier.HARD_HAT -> throw IllegalArgumentException("$modifier is not valid") // should not happen
            DamageModifier.BLOCKING -> getBlockingDamageReduction(event, damage)
            DamageModifier.ARMOR -> getArmorDamageReduction(event, damage)
            DamageModifier.RESISTANCE -> getResistanceDamageReduction(event, damage)
            DamageModifier.MAGIC -> getMagicDamageReduction(event, damage)
            DamageModifier.ABSORPTION -> getAbsorptionDamageReduction(event, damage)
        }

    fun getBlockingDamageReduction(event: DamageCalculationEvent, damage: Double) = if (event.damageBlocked) damage else 0.0

    fun getArmorDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.getArmorDamageReduction(event.damaged, damage)
        return damage * reduction
    }

    fun getResistanceDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.getResistancePotionEffectReduction(event.damaged)
        return damage * reduction
    }

    /** armor enchants and potion resistance */
    fun getMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.getProtectionDamageReduction(event.damaged)
        return damage * reduction
    }

    fun getAbsorptionDamageReduction(event: DamageCalculationEvent, damage: Double) = DamageUtil.getAbsorbedDamage(event.damaged, damage)

    private fun buildBaseDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damager = EventUtil.getLivingEntityDamager(event) ?: return null
        val customAttributes = DamageUtil.getEntityCustomAttributes(damager)
        val damagedEntity = event.entity as? LivingEntity ?: return null
        val cause = event.cause
        val isDungeonWorld = WorldUtil.isDungeonWorld(event.damager.world)
        val damageBlocked = event.getDamage(DamageModifier.BLOCKING) < 0
        val difficulty = damager.world.difficulty

        return DamageCalculationEvent(damager, customAttributes, damagedEntity, cause, isDungeonWorld, damageBlocked, difficulty)
    }
}