package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object EntityAttackDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK

    override fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildBaseDamageEvent(event) ?: return null

        val baseDamage = DamageUtil.getMeleeBaseDamage(damageEvent.damager)
        val enchantDamage = DamageUtil.getMeleeEnchantDamage(damageEvent.damager, damageEvent.damaged)

        damageEvent.baseDamage.add(baseDamage)
        damageEvent.enchantDamage = enchantDamage
        damageEvent.isCritical = DamageUtil.isMeleeCritical(damageEvent.damager)

        if (damageEvent.isCritical) {
            damageEvent.criticalRoll = 0.5
        }

        return damageEvent
    }

    override fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildBaseDamageEvent(event) ?: return null

        val rawDamage = DamageUtil.reverseDifficultyDamage(damageEvent.difficulty, event.damage)
        val strengthDamage = DamageUtil.getStrengthDamage(damageEvent.damager)
        val enchantDamage = DamageUtil.getMeleeEnchantDamage(damageEvent.damager, damageEvent.damaged)
        val baseDamage = rawDamage - strengthDamage - enchantDamage

        damageEvent.baseDamage.add(baseDamage)
        damageEvent.enchantDamage = DamageUtil.getMeleeEnchantDamage(damageEvent.damager, damageEvent.damaged)

        return damageEvent
    }

    override fun getNormalBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageUtil.getRawBaseDamage(event)
        damage += DamageUtil.getStrengthDamage(event.damager)

        if (event.isCritical) {
            damage *= 1 + event.criticalRoll
        }

        damage *= DamageUtil.getAttackCooldownMultiplier(event.damager)

        var enchantDamage = event.enchantDamage
        enchantDamage *= DamageUtil.getEnchantAttackCooldownMultiplier(event.damager)
        damage += enchantDamage

        return damage
    }
}