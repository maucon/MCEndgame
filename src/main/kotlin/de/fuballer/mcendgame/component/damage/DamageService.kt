package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import kotlin.math.max
import kotlin.math.min

@Component
class DamageService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.damager.world)) return
        val player = EventUtil.getPlayerDamager(event) ?: return
        val damagedEntity = event.entity as? LivingEntity ?: return

        val isProjectile = event.damager is Projectile

        val baseDamage = if (isProjectile) event.damage else getMeleeBaseDamage(player)
        val isCritical = isCritical(isProjectile, event.damager, player)

        val damageEvent = DamageCalculationEvent(player, damagedEntity)
        damageEvent.baseDamage.add(baseDamage)
        damageEvent.isProjectile = isProjectile
        damageEvent.isCritical = isCritical
        damageEvent.attackCooldown = player.attackCooldown.toDouble()
        EventGateway.apply(damageEvent)

        val rawDamage = calculateRawDamage(damageEvent)
        val reducedDamage = calculateReducedDamage(damagedEntity, rawDamage, damageEvent.isProjectile)
        val absorbedDamage = getAbsorbedDamage(player, reducedDamage)

        EntityDamageEvent.DamageModifier.entries.toTypedArray()
            .filter { event.isApplicable(it) }
            .forEach { event.setDamage(it, 0.0) }

        event.setDamage(EntityDamageEvent.DamageModifier.ABSORPTION, absorbedDamage)
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, reducedDamage - absorbedDamage)
    }

    private fun isCritical(isProjectile: Boolean, damager: Entity, player: Player): Boolean {
        if (isProjectile) {
            if (damager !is Arrow) return false
            return damager.isCritical
        }
        return isMeleeCritical(player)
    }

    private fun getAbsorbedDamage(player: Player, damage: Double) = max(player.absorptionAmount, damage)

    private fun calculateRawDamage(event: DamageCalculationEvent): Double {
        var calculatedDamage = event.baseDamage.sum()
        calculatedDamage *= event.increasedDamage.sum()
        event.moreDamage.forEach { calculatedDamage *= it }

        calculatedDamage += getStrengthDamage(event.player)

        if (event.isCritical) calculatedDamage *= 1.5
        calculatedDamage *= event.attackCooldown

        return calculatedDamage
    }

    private fun calculateReducedDamage(target: LivingEntity, damage: Double, isProjectileDamage: Boolean): Double {
        var reducedDamage = damage

        reducedDamage *= 1 - getArmorDamageReduction(target, damage)
        reducedDamage *= 1 - getEnchantmentDamageReduction(target, isProjectileDamage)

        val resistanceLevel = (target.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)?.amplifier ?: -1) + 1
        reducedDamage *= 1 - (resistanceLevel * 0.2)

        return reducedDamage
    }

    private fun getArmorDamageReduction(target: LivingEntity, damage: Double): Double {
        val armor = target.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
        val armorToughness = target.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value ?: 0.0

        val minEffectiveArmor = armor / 5
        val reducedArmor = (4 * damage / (armorToughness + 8))
        val effectiveArmor = max(minEffectiveArmor, armor - reducedArmor)

        return min(20.0, effectiveArmor) / 25
    }

    private fun getEnchantmentDamageReduction(target: LivingEntity, isProjectileDamage: Boolean): Double {
        val equipment = target.equipment ?: return 0.0

        var protectionLevel = equipment.helmet?.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0
        protectionLevel += equipment.chestplate?.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0
        protectionLevel += equipment.leggings?.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0
        protectionLevel += equipment.boots?.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0

        var damageReduction = protectionLevel * 0.04

        if (isProjectileDamage) {
            var projectileProtectionLevel = equipment.helmet?.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) ?: 0
            projectileProtectionLevel += equipment.chestplate?.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) ?: 0
            projectileProtectionLevel += equipment.leggings?.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) ?: 0
            projectileProtectionLevel += equipment.boots?.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) ?: 0

            damageReduction += projectileProtectionLevel * 0.08
        }

        return min(0.8, damageReduction)
    }

    private fun getMeleeBaseDamage(player: Player): Double {
        var baseDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)!!.value

        baseDamage -= getStrengthDamage(player)

        val mainHandItem = player.equipment!!.getItem(EquipmentSlot.HAND)
        baseDamage += getCombinedSharpnessDamage(mainHandItem)

        return baseDamage
    }

    private fun getStrengthDamage(player: Player): Double {
        val strengthLevel = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)?.amplifier ?: -1
        return (strengthLevel + 1) * 3.0
    }

    private fun getCombinedSharpnessDamage(item: ItemStack): Double {
        val combinedLevel = getCombinedSharpnessLevel(item)
        if (combinedLevel == 0) return 0.0
        return combinedLevel * 0.5 + 0.5
    }

    private fun getCombinedSharpnessLevel(item: ItemStack): Int {
        var combinedLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL)
        combinedLevel += item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)
        combinedLevel += item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)
        combinedLevel += item.getEnchantmentLevel(Enchantment.IMPALING)

        return combinedLevel
    }

    private fun isMeleeCritical(player: Player): Boolean {
        if (player.fallDistance <= 0) return false
        if (player.isOnGround) return false
        if (player.isClimbing) return false
        if (player.isInWater) return false
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) return false
        if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) return false
        if (player.isInsideVehicle) return false
        if (player.velocity.x > 0 || player.velocity.z > 0) return false
        if (player.attackCooldown < 0.9) return false
        return true
    }
}