package de.fuballer.mcendgame.component.damage.dmg

import de.fuballer.mcendgame.component.damage.DamageCalculation
import de.fuballer.mcendgame.event.DamageCalculationEvent
import org.bukkit.event.entity.EntityDamageEvent

interface DamageTypeCalculator {
    val damageType: EntityDamageEvent.DamageCause

    fun getBaseDamage(event: DamageCalculationEvent) = 0.0 // TODO

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
        val reduction = DamageCalculation.armorDamageReduction(event.damaged, damage)
        return damage * reduction
    }

    fun getResistanceDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageCalculation.resistancePotionEffectReduction(event.damaged)
        return damage * reduction
    }

    /** armor enchants and potion resistance */
    fun getMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = DamageCalculation.protectionDamageReduction(event.damaged)
        return damage * reduction
    }

    fun getAbsorptionDamageReduction(event: DamageCalculationEvent, damage: Double) = DamageCalculation.absorbedDamage(event.damaged, damage)
}