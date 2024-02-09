package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RolledAttribute
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.getBaseDamage
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object DamageUtil {
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

    fun customPlayerAttributes(player: Player): Map<AttributeType, List<Double>> {
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

    fun projectileBaseDamage(projectile: Projectile, event: EntityDamageByEntityEvent): Double {
        val baseDamage = projectile.getBaseDamage() ?: return event.damage
        return baseDamage * projectile.velocity.length()
    }

    fun meleeBaseDamage(player: Player): Double {
        val baseDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)!!.value
        return baseDamage - strengthDamage(player)
    }

    fun meleeEnchantDamage(player: Player, damagedEntity: LivingEntity, isDungeonWorld: Boolean): Double {
        val mainHandItem = player.equipment!!.getItem(EquipmentSlot.HAND)

        if (isDungeonWorld) return combinedSharpnessDamage(mainHandItem)

        return typeBasedEnchantmentDamage(mainHandItem, damagedEntity)
    }

    fun sweepingEdgeMultiplier(item: ItemStack): Double {
        val sweepingLevel = item.getEnchantmentLevel(Enchantment.SWEEPING_EDGE)
        return sweepingLevel / (sweepingLevel + 1.0)
    }

    fun isProjectileCritical(projectile: Projectile): Boolean {
        val arrow = projectile as? Arrow ?: return false
        return arrow.isCritical
    }

    fun isMeleeCritical(player: Player): Boolean {
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

    private fun combinedSharpnessDamage(item: ItemStack): Double {
        val combinedLevel = combinedSharpnessLevel(item)
        if (combinedLevel == 0) return 0.0
        return combinedLevel * 0.5 + 0.5
    }

    private fun combinedSharpnessLevel(item: ItemStack): Int {
        var combinedLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL)
        combinedLevel += item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)
        combinedLevel += item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)
        combinedLevel += item.getEnchantmentLevel(Enchantment.IMPALING)

        return combinedLevel
    }

    private fun typeBasedEnchantmentDamage(item: ItemStack, damagedEntity: LivingEntity): Double {
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

    private fun totalEquipmentEnchantmentLevel(entity: LivingEntity, enchantment: Enchantment): Int {
        val equipment = entity.equipment ?: return 0

        var level = equipment.helmet?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.chestplate?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.leggings?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.boots?.getEnchantmentLevel(enchantment) ?: 0

        return level
    }
}