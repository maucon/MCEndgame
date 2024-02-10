package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot

object EntitySweepDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK

    override fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildDamageEventForPlayer(event) ?: return null

        val baseDamage = DamageUtil.getMeleeBaseDamage(damageEvent.damager)
        val enchantDamage = DamageUtil.getMeleeEnchantDamage(damageEvent.damager, damageEvent.damaged, damageEvent.isDungeonWorld)
        val mainHandItem = damageEvent.damager.equipment!!.getItem(EquipmentSlot.HAND)

        damageEvent.baseDamage.add(baseDamage)
        damageEvent.enchantDamage = enchantDamage
        damageEvent.sweepingEdgeMultiplier = DamageUtil.getSweepingEdgeMultiplier(mainHandItem)

        return damageEvent
    }

    override fun getNormalBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageUtil.getRawBaseDamage(event) ?: return 0.0

        damage += DamageUtil.getStrengthDamage(event.damager)

        if (event.isCritical) {
            damage *= 1.5
        }

        damage += event.enchantDamage
        damage = 1 + damage * event.sweepingEdgeMultiplier

        return damage
    }
}