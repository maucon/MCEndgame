package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Difficulty
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier

interface DamageCauseCalculator {
    val damageType: DamageCause

    fun buildDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = buildBaseDamageEvent(event) ?: return null

        if (event.damager is Player) return buildDamageEventForPlayer(event, damageEvent)
        return buildDamageEventForNonPlayer(event, damageEvent)
    }

    fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        damageEvent.baseDamage.add(event.damage)
        return damageEvent
    }

    fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        damageEvent.baseDamage.add(event.damage)
        return damageEvent
    }

    fun getBaseDamage(event: DamageCalculationEvent) =
        if (event.damager is Player) getBaseDamageForPlayer(event)
        else getBaseDamageForNonPlayer(event)

    fun getBaseDamageForPlayer(event: DamageCalculationEvent): Double {
        val normalBaseDamage = getNormalBaseDamage(event)
        return scaleInvulnerabilityDamage(event.damaged, normalBaseDamage)
    }

    fun getBaseDamageForNonPlayer(event: DamageCalculationEvent): Double {
        val normalBaseDamage = getNormalBaseDamage(event)
        val baseDamage = scaleDifficultyDamage(event.difficulty, normalBaseDamage)
        return scaleInvulnerabilityDamage(event.damaged, baseDamage)
    }

    fun getNormalBaseDamage(event: DamageCalculationEvent) = DamageUtil.getRawBaseDamage(event)

    fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double) = DamageUtil.scaleInvulnerabilityDamage(entity, damage)

    fun scaleDifficultyDamage(difficulty: Difficulty, damage: Double) = DamageUtil.scaleDifficultyDamage(difficulty, damage)

    fun getFlatDamageReduction(event: DamageCalculationEvent, damage: Double, modifier: DamageModifier) =
        when (modifier) {
            DamageModifier.BASE -> throw IllegalArgumentException("$modifier is not a damage reduction")
            DamageModifier.HARD_HAT -> throw IllegalArgumentException("$modifier is not valid") // should not happen
            DamageModifier.BLOCKING -> getFlatBlockingDamageReduction(event, damage)
            DamageModifier.ARMOR -> getFlatArmorDamageReduction(event, damage)
            DamageModifier.RESISTANCE -> getFlatResistanceDamageReduction(event, damage)
            DamageModifier.MAGIC -> getFlatMagicDamageReduction(event, damage)
            DamageModifier.ABSORPTION -> getFlatAbsorptionDamageReduction(event, damage)
        }

    fun getFlatBlockingDamageReduction(event: DamageCalculationEvent, damage: Double) = if (event.damageBlocked) damage else 0.0

    fun getFlatArmorDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.getReducedDamageByArmor(event.damaged, damage)
        return damage * reduction
    }

    fun getFlatResistanceDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.getResistancePotionEffectReduction(event.damaged)
        return damage * reduction
    }

    /** armor enchants and potion resistance */
    fun getFlatMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageUtil.getProtectionDamageReduction(event.damaged)
        return damage * reduction
    }

    fun getFlatAbsorptionDamageReduction(event: DamageCalculationEvent, damage: Double) = DamageUtil.getAbsorbedDamage(event.damaged, damage)

    private fun buildBaseDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damager = EntityUtil.getLivingEntityDamager(event.damager) ?: return null
        val customAttributes = DamageUtil.getEntityCustomAttributes(damager)
        val damagedEntity = event.entity as? LivingEntity ?: return null
        val cause = event.cause
        val isDungeonWorld = event.damager.world.isDungeonWorld()
        val damageBlocked = event.getDamage(DamageModifier.BLOCKING) < 0
        val difficulty = damager.world.difficulty

        return DamageCalculationEvent(damager, customAttributes, damagedEntity, cause, isDungeonWorld, damageBlocked, difficulty)
    }
}