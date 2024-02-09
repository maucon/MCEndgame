package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.event.DamageCalculationEvent
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object DamageCalculation {
    /** flat, increase, more */
    fun rawBaseDamage(event: DamageCalculationEvent): Double? {
        if (event.nullifyDamage) return null

        var damage = event.baseDamage.sum() // flat
        damage *= 1 + event.increasedDamage.sum() // increase
        event.moreDamage.forEach { damage *= 1 + it } // more

        return damage
    }

    fun strengthDamage(player: Player): Double {
        val strengthLevel = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)?.amplifier ?: return 0.0
        return (strengthLevel + 1) * 3.0
    }

    fun attackCooldownMultiplier(player: Player) = 0.2 + player.attackCooldown.pow(2) * 0.8

    fun enchantAttackCooldownMultiplier(player: Player) = player.attackCooldown.toDouble()

    fun armorDamageReduction(entity: LivingEntity, damage: Double): Double {
        var armor = entity.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
        val armorToughness = entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value ?: 0.0
        armor = floor(armor)

        val minEffectiveArmor = armor / 5
        val reducedArmor = (4 * damage / (armorToughness + 8))
        val effectiveArmor = max(minEffectiveArmor, armor - reducedArmor)

        return min(20.0, effectiveArmor) / 25
    }

    fun protectionDamageReduction(entity: LivingEntity): Double {
        val protectionLevel = totalEquipmentEnchantmentLevel(entity, Enchantment.PROTECTION_ENVIRONMENTAL)
        val damageReduction = protectionLevel * 0.04
        return min(0.8, damageReduction)
    }

    fun specialEnchantDamageReduction(entity: LivingEntity, enchantment: Enchantment): Double {
        val protectionReduction = protectionDamageReduction(entity)
        val specialEnchantLevel = totalEquipmentEnchantmentLevel(entity, enchantment)
        val specialEnchantReduction = specialEnchantLevel * 0.08

        return min(0.8, protectionReduction + specialEnchantReduction)
    }

    fun resistancePotionEffectReduction(entity: LivingEntity): Double {
        val amplifier = entity.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)?.amplifier ?: return 0.0
        return min(1.0, (amplifier + 1) * 0.2)
    }

    fun invulnerabilityDamage(entity: LivingEntity, damage: Double): Double {
        if (entity.noDamageTicks <= 10) return damage
        return damage - entity.lastDamage
    }

    fun absorbedDamage(entity: LivingEntity, damage: Double) = max(0.0, min(entity.absorptionAmount, damage))

    private fun totalEquipmentEnchantmentLevel(entity: LivingEntity, enchantment: Enchantment): Int {
        val equipment = entity.equipment ?: return 0

        var level = equipment.helmet?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.chestplate?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.leggings?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.boots?.getEnchantmentLevel(enchantment) ?: 0

        return level
    }
}