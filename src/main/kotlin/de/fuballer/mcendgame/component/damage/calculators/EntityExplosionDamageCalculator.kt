package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object EntityExplosionDamageCalculator : DamageCauseCalculator {
    override val damageType = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION

    override fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        val rawDamage = DamageUtil.reverseDifficultyDamage(damageEvent.difficulty, event.damage)
        damageEvent.baseDamage.add(rawDamage)
        return damageEvent
    }

    override fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double) = damage

    override fun getFlatMagicDamageReduction(event: DamageCalculationEvent, damage: Double) =
        damage * DamageUtil.getSpecialEnchantDamageReduction(event.damaged, Enchantment.PROTECTION_EXPLOSIONS)
}