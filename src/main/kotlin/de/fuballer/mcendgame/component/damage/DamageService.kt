package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RolledAttribute
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.getBaseDamage
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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import kotlin.math.*
import kotlin.random.Random

@Component
class DamageService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        println("_____________\n" + event.damage + " / " + event.finalDamage)

        val isDungeonWorld = WorldUtil.isDungeonWorld(event.damager.world)

        val player = EventUtil.getPlayerDamager(event) ?: return
        val damagedEntity = event.entity as? LivingEntity ?: return

        println(damagedEntity.noDamageTicks)

        val isProjectile = event.cause == DamageCause.PROJECTILE

        val isCritical = isCritical(isProjectile, event.damager, player)
        val baseDamage = if (isProjectile) getProjectileBaseDamage(event.damager as Projectile, event) else getMeleeBaseDamage(player)
        val enchantDamage = if (isProjectile) 0.0 else getMeleeEnchantDamage(player, damagedEntity, isDungeonWorld)

        val customPlayerAttributes = getCustomPlayerAttributes(player)
        val damageEvent = DamageCalculationEvent(player = player, customPlayerAttributes = customPlayerAttributes, damaged = damagedEntity, cause = event.cause)
        damageEvent.baseDamage.add(baseDamage)
        damageEvent.enchantDamage = enchantDamage
        damageEvent.isCritical = isCritical
        damageEvent.attackCooldown = player.attackCooldown.toDouble()

        if (event.cause == DamageCause.ENTITY_SWEEP_ATTACK) {
            val mainHandItem = player.equipment!!.getItem(EquipmentSlot.HAND)
            damageEvent.sweepingEdgeMultiplier = getSweepingEdgeMultiplier(mainHandItem)
        }

        EventGateway.apply(damageEvent)

        var rawDamage = calculateRawDamage(damageEvent)
        rawDamage = calculateInvulnerabilityDamage(rawDamage, damagedEntity)
        val reducedDamage = calculateReducedDamage(damagedEntity, rawDamage, damageEvent.cause == DamageCause.PROJECTILE)

        println("$rawDamage / $reducedDamage")

        val absorbedDamage = getAbsorbedDamage(damagedEntity, reducedDamage)

        EntityDamageEvent.DamageModifier.entries.toTypedArray()
            .filter { event.isApplicable(it) }
            .forEach { event.setDamage(it, 0.0) }

        event.setDamage(EntityDamageEvent.DamageModifier.ABSORPTION, absorbedDamage)
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, reducedDamage - absorbedDamage)
    }

    private fun calculateInvulnerabilityDamage(damage: Double, target: LivingEntity): Double {
        if (target.noDamageTicks <= 10) return damage
        return damage - target.lastDamage
    }

    private fun getCustomPlayerAttributes(player: Player): Map<AttributeType, List<Double>> {
        val attributes: MutableList<RolledAttribute> = mutableListOf()

        val equipment = player.equipment ?: return mapOf()

        equipment.helmet?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.chestplate?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.leggings?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.boots?.getRolledAttributes()?.let { attributes.addAll(it) }

        var customItem = equipment.itemInMainHand.getCustomItemType()
        if (customItem != null) {
            val customItemEquipment = customItem.equipment
            if (customItemEquipment.slot == EquipmentSlot.HAND || !customItemEquipment.extraAttributesInSlot) {
                equipment.itemInMainHand.getRolledAttributes()?.let { attributes.addAll(it) }
            }
        }

        customItem = equipment.itemInOffHand.getCustomItemType()
        if (customItem != null) {
            val customItemEquipment = customItem.equipment
            if (customItemEquipment.slot == EquipmentSlot.OFF_HAND || !customItemEquipment.extraAttributesInSlot) {
                equipment.itemInOffHand.getRolledAttributes()?.let { attributes.addAll(it) }
            }
        }

        return attributes.filter { it.type.applicableAttributeType == null }
            .groupBy { it.type }
            .mapValues { (_, values) -> values.map { it.roll } }
    }

    private fun getAbsorbedDamage(damagedEntity: LivingEntity, damage: Double) = max(damagedEntity.absorptionAmount, damage)

    private fun calculateReducedDamage(target: LivingEntity, damage: Double, isProjectileDamage: Boolean): Double {
        var reducedDamage = damage

        reducedDamage *= 1 - getArmorDamageReduction(target, damage)
        reducedDamage *= 1 - getEnchantmentDamageReduction(target, isProjectileDamage)

        val resistanceLevel = (target.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)?.amplifier ?: -1) + 1
        reducedDamage *= 1 - (resistanceLevel * 0.2)

        return reducedDamage
    }

    private fun getArmorDamageReduction(target: LivingEntity, damage: Double): Double {
        var armor = target.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
        armor = floor(armor)
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

    private fun getProjectileBaseDamage(projectile: Projectile, event: EntityDamageByEntityEvent): Double {
        val baseDamage = projectile.getBaseDamage() ?: return event.damage
        return baseDamage * projectile.velocity.length()
    }

    private fun getMeleeBaseDamage(player: Player): Double {
        val baseDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)!!.value
        return baseDamage - getStrengthDamage(player)
    }

    private fun getMeleeEnchantDamage(player: Player, damagedEntity: LivingEntity, isDungeonWorld: Boolean): Double {
        val mainHandItem = player.equipment!!.getItem(EquipmentSlot.HAND)

        if (isDungeonWorld) return getCombinedSharpnessDamage(mainHandItem)

        return getTypeBasedEnchantmentDamage(mainHandItem, damagedEntity)
    }

    private fun calculateRawDamage(event: DamageCalculationEvent): Double {
        if (event.nullifyDamage) return 0.0

        var calculatedDamage = event.baseDamage.sum()
        calculatedDamage *= 1 + event.increasedDamage.sum()
        event.moreDamage.forEach { calculatedDamage *= 1 + it }

        calculatedDamage += getStrengthDamage(event.player)

        if (event.isCritical) {
            if (event.cause != DamageCause.PROJECTILE) {
                calculatedDamage *= 1.5
            } else {
                calculatedDamage += Random.nextDouble() * (calculatedDamage * 1.5 + 2 - calculatedDamage)
            }
        }

        if (event.cause == DamageCause.PROJECTILE) return ceil(calculatedDamage)

        var enchantDamage = event.enchantDamage

        if (event.cause != DamageCause.ENTITY_SWEEP_ATTACK) {
            calculatedDamage *= getAttackCooldownMultiplier(event.player)
            enchantDamage *= getEnchantAttackCooldownMultiplier(event.player)
        }

        calculatedDamage += enchantDamage

        if (event.cause == DamageCause.ENTITY_SWEEP_ATTACK) {
            calculatedDamage = 1 + calculatedDamage * event.sweepingEdgeMultiplier
        }

        return calculatedDamage
    }

    private fun getAttackCooldownMultiplier(player: Player) = 0.2 + player.attackCooldown.pow(2) * 0.8

    private fun getEnchantAttackCooldownMultiplier(player: Player) = player.attackCooldown.toDouble()

    private fun getStrengthDamage(player: Player): Double {
        val strengthLevel = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)?.amplifier ?: -1
        return (strengthLevel + 1) * 3.0
    }

    private fun getSweepingEdgeMultiplier(item: ItemStack): Double {
        val sweepingLevel = item.getEnchantmentLevel(Enchantment.SWEEPING_EDGE)
        return sweepingLevel / (sweepingLevel + 1.0)
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

    private fun getTypeBasedEnchantmentDamage(item: ItemStack, damagedEntity: LivingEntity): Double {
        var damage = 0.0

        val sharpnessLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL)
        if (sharpnessLevel > 0)
            damage += sharpnessLevel * 0.5 + 0.5

        if (damagedEntity.category == EntityCategory.UNDEAD) {
            val smiteLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)
            if (smiteLevel > 0)
                return damage + smiteLevel * 2.5
        }
        if (damagedEntity.category == EntityCategory.ARTHROPOD) {
            val baneLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)
            if (baneLevel > 0)
                return damage + baneLevel * 2.5
        }
        if (damagedEntity.category == EntityCategory.WATER) {
            val impalingLevel = item.getEnchantmentLevel(Enchantment.IMPALING)
            if (impalingLevel > 0)
                return damage + impalingLevel * 2.5
        }

        return damage
    }

    private fun isCritical(isProjectile: Boolean, damager: Entity, player: Player): Boolean {
        if (isProjectile) {
            if (damager !is Arrow) return false
            return damager.isCritical
        }
        return isMeleeCritical(player)
    }

    private fun isMeleeCritical(player: Player): Boolean {
        if (player.fallDistance <= 0) return false
        if (player.isOnGround) return false
        if (player.isClimbing) return false
        if (player.isInWater) return false
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) return false
        if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) return false
        if (player.isInsideVehicle) return false
        if (player.isSprinting) return false
        if (player.attackCooldown < 0.9) return false
        return true
    }
}