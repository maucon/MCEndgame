package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot

object EntitySweepDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK

    override fun buildDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = super.buildDamageEvent(event) ?: return null

        val baseDamage = DamageUtil.getMeleeBaseDamage(damageEvent.player)
        val enchantDamage = DamageUtil.getMeleeEnchantDamage(damageEvent.player, damageEvent.damaged, damageEvent.isDungeonWorld)
        val mainHandItem = damageEvent.player.equipment!!.getItem(EquipmentSlot.HAND)

        damageEvent.baseDamage.add(baseDamage)
        damageEvent.enchantDamage = enchantDamage
        damageEvent.sweepingEdgeMultiplier = DamageUtil.getSweepingEdgeMultiplier(mainHandItem)

        return damageEvent
    }

    override fun getBaseDamage(event: DamageCalculationEvent): Double {
        var damage = DamageUtil.getRawBaseDamage(event) ?: return 0.0

        damage += DamageUtil.getStrengthDamage(event.player)

        if (event.isCritical) {
            damage *= 1.5
        }

        damage += event.enchantDamage
        damage = 1 + damage * event.sweepingEdgeMultiplier

        return damage
    }
}