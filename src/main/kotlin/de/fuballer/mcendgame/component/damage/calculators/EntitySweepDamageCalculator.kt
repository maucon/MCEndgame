package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot

object EntitySweepDamageCalculator : DamageCauseCalculator() {
    private const val CRITICAL_MULTIPLIER = 1.5

    override val damageType = EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
    override val canBeBlocked = true
    override val affectedByInvulnerability = true
    override val affectedByArmor = true
    override val scaledByDifficulty = true
    override val affectedByArmorProtection = true

    override fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        val baseDamage = DamageUtil.getMeleeBaseDamage(damageEvent.damager)
        val enchantDamage = DamageUtil.getMeleeEnchantDamage(damageEvent.damager, damageEvent.damaged)
        val mainHandItem = damageEvent.damager.equipment!!.getItem(EquipmentSlot.HAND)

        damageEvent.baseDamage.add(baseDamage)
        damageEvent.enchantDamage = enchantDamage
        damageEvent.sweepingEdgeMultiplier = DamageUtil.getSweepingEdgeMultiplier(mainHandItem)

        return damageEvent
    }

    override fun getNormalBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageUtil.getRawBaseDamage(event)

        damage += DamageUtil.getStrengthDamage(event.damager)

        if (event.isCritical) {
            damage *= CRITICAL_MULTIPLIER
        }

        damage += event.enchantDamage
        damage = 1 + damage * event.sweepingEdgeMultiplier

        return damage
    }
}