package de.fuballer.mcendgame.component.damage.calculators

import org.bukkit.enchantments.Enchantment
import org.bukkit.event.entity.EntityDamageEvent

object EntityExplosionDamageCalculator : DamageCauseCalculator() {
    override val damageType = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
    override val canBeBlocked = true
    override val affectedByInvulnerability = false
    override val affectedByArmor = true
    override val scaledByDifficulty = true
    override val affectedByArmorProtection = true
    override val specialEnchantDamageReduction: Enchantment = Enchantment.PROTECTION_EXPLOSIONS
}