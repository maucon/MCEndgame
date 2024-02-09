package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

interface DamageCauseCalculator {
    val damageType: EntityDamageEvent.DamageCause

    fun buildDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val player = EventUtil.getPlayerDamager(event) ?: return null // TODO any entity
        val damagedEntity = event.entity as? LivingEntity ?: return null // TODO any entity
        val customPlayerAttributes = DamageUtil.getCustomPlayerAttributes(player)
        val cause = event.cause
        val isDungeonWorld = WorldUtil.isDungeonWorld(event.damager.world)

        return DamageCalculationEvent(player, customPlayerAttributes, damagedEntity, cause, isDungeonWorld)
    }

    fun getBaseDamage(event: DamageCalculationEvent) = DamageUtil.getRawBaseDamage(event) ?: 0.0

    fun getInvulnerabilityDamage(entity: LivingEntity, damage: Double) = DamageUtil.getInvulnerabilityDamage(entity, damage)

    fun getDamageReduction(event: DamageCalculationEvent, damage: Double, modifier: EntityDamageEvent.DamageModifier) =
        when (modifier) {
            EntityDamageEvent.DamageModifier.BASE -> throw IllegalArgumentException("${EntityDamageEvent.DamageModifier.BASE} is not a damage reduction")
            EntityDamageEvent.DamageModifier.HARD_HAT -> getHardHatDamageReduction(event, damage)
            EntityDamageEvent.DamageModifier.BLOCKING -> getBlockingDamageReduction(event, damage)
            EntityDamageEvent.DamageModifier.ARMOR -> getArmorDamageReduction(event, damage)
            EntityDamageEvent.DamageModifier.RESISTANCE -> getResistanceDamageReduction(event, damage)
            EntityDamageEvent.DamageModifier.MAGIC -> getMagicDamageReduction(event, damage)
            EntityDamageEvent.DamageModifier.ABSORPTION -> getAbsorptionDamageReduction(event, damage)
        }

    fun getHardHatDamageReduction(event: DamageCalculationEvent, damage: Double) = 0.0 // TODO
    fun getBlockingDamageReduction(event: DamageCalculationEvent, damage: Double) = 0.0 // TODO

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
}