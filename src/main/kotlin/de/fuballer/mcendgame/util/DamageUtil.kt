package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RolledAttribute
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getRolledAttributes
import org.bukkit.Difficulty
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object DamageUtil {
    /** flat, increase, more */
    fun getRawBaseDamage(event: DamageCalculationEvent): Double {
        var damage = event.baseDamage.sum() // flat
        damage *= 1 + event.increasedDamage.sum() // increase
        event.moreDamage.forEach { damage *= 1 + it } // more

        return damage
    }

    fun getStrengthDamage(entity: LivingEntity): Double {
        val strengthLevel = entity.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)?.amplifier ?: return 0.0
        return (strengthLevel + 1) * 3.0
    }

    fun getAttackCooldownMultiplier(entity: LivingEntity): Double {
        val player = entity as? Player ?: return 1.0
        return 0.2 + player.attackCooldown.pow(2) * 0.8
    }

    fun getEnchantAttackCooldownMultiplier(entity: LivingEntity): Double {
        val player = entity as? Player ?: return 1.0
        return player.attackCooldown.toDouble()
    }

    fun getReducedDamageByArmor(entity: LivingEntity, damage: Double): Double {
        var armor = entity.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
        val armorToughness = entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value ?: 0.0
        armor = floor(armor)

        val minEffectiveArmor = armor / 5
        val reducedArmor = (4 * damage / (armorToughness + 8))
        val effectiveArmor = max(minEffectiveArmor, armor - reducedArmor)

        return min(20.0, effectiveArmor) / 25
    }

    fun getProtectionDamageReduction(entity: LivingEntity): Double {
        val protectionLevel = getTotalEquipmentEnchantmentLevel(entity, Enchantment.PROTECTION_ENVIRONMENTAL)
        val damageReduction = protectionLevel * 0.04
        return min(0.8, damageReduction)
    }

    fun getSpecialEnchantDamageReduction(entity: LivingEntity, enchantment: Enchantment): Double {
        val protectionReduction = getProtectionDamageReduction(entity)
        val specialEnchantLevel = getTotalEquipmentEnchantmentLevel(entity, enchantment)
        val specialEnchantReduction = specialEnchantLevel * 0.08

        return min(0.8, protectionReduction + specialEnchantReduction)
    }

    fun getResistancePotionEffectReduction(entity: LivingEntity): Double {
        val amplifier = entity.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)?.amplifier ?: return 0.0
        return min(1.0, (amplifier + 1) * 0.2)
    }

    // FIXME works most of the time 🤨
    fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double): Double {
        if (entity.noDamageTicks <= 10) return damage
        println("Invulnerability: ${entity.noDamageTicks} | ${entity.lastDamageCause?.cause} | ${entity.lastDamage}")

        entity.lastDamageCause?.let {
            if (it.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && it.cause != EntityDamageEvent.DamageCause.PROJECTILE
            ) return damage
        }

        if (damage > entity.lastDamage) return damage - entity.lastDamage
        return damage
    }

    fun scaleDifficultyDamage(difficulty: Difficulty, damage: Double) = // TODO
        when (difficulty) {
            Difficulty.PEACEFUL -> 0.0
            Difficulty.EASY -> min(damage / 2 + 1, damage)
            Difficulty.NORMAL -> damage
            Difficulty.HARD -> damage * 1.5
        }

    fun reverseDifficultyDamage(difficulty: Difficulty, damage: Double) = // TODO
        when (difficulty) {
            Difficulty.PEACEFUL -> 0.0
            Difficulty.EASY -> if (damage <= 2) damage else (damage - 1) * 2
            Difficulty.NORMAL -> damage
            Difficulty.HARD -> damage / 1.5
        }

    fun getAbsorbedDamage(entity: LivingEntity, damage: Double) = max(0.0, min(entity.absorptionAmount, damage))

    fun getEntityCustomAttributes(entity: LivingEntity): Map<AttributeType, List<Double>> {
        val attributes: MutableList<RolledAttribute> = mutableListOf()
        val equipment = entity.equipment ?: return mapOf()

        equipment.helmet?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.chestplate?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.leggings?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.boots?.getRolledAttributes()?.let { attributes.addAll(it) }

        equipment.itemInMainHand.getCustomItemType()?.also { customItemType ->
            val customItemEquipment = customItemType.equipment
            if (customItemEquipment.slot == EquipmentSlot.HAND || !customItemEquipment.extraAttributesInSlot) {
                equipment.itemInMainHand.getRolledAttributes()?.let { attributes.addAll(it) }
            }
        }

        equipment.itemInOffHand.getCustomItemType()?.also { customItemType ->
            val customItemEquipment = customItemType.equipment
            if (customItemEquipment.slot == EquipmentSlot.OFF_HAND || !customItemEquipment.extraAttributesInSlot) {
                equipment.itemInOffHand.getRolledAttributes()?.let { attributes.addAll(it) }
            }
        }

        return attributes.filter { it.type.applicableAttributeType == null }
            .groupBy { it.type }
            .mapValues { (_, values) -> values.map { it.roll } }
    }

    fun getProjectileBaseDamage(projectile: Projectile): Double {
        val arrow = projectile as? AbstractArrow ?: return 0.0
        return arrow.damage
    }

    fun getMeleeBaseDamage(entity: LivingEntity): Double {
        val baseDamage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)!!.value
        return baseDamage - getStrengthDamage(entity)
    }

    fun getMeleeEnchantDamage(entity: LivingEntity, damagedEntity: LivingEntity): Double {
        val mainHandItem = entity.equipment!!.getItem(EquipmentSlot.HAND)
        return getTypeBasedEnchantmentDamage(mainHandItem, damagedEntity)
    }

    fun getSweepingEdgeMultiplier(item: ItemStack): Double {
        val sweepingLevel = item.getEnchantmentLevel(Enchantment.SWEEPING_EDGE)
        return sweepingLevel / (sweepingLevel + 1.0)
    }

    fun isProjectileCritical(projectile: Projectile): Boolean {
        val arrow = projectile as? Arrow ?: return false
        return arrow.isCritical
    }

    fun isMeleeCritical(entity: LivingEntity): Boolean {
        val player = entity as? Player ?: return false

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

    fun getCombinedMeleeEnchantDamage(entity: LivingEntity): Double {
        val item = entity.equipment!!.getItem(EquipmentSlot.HAND)
        val combinedLevel = getCombinedMeleeEnchantLevel(item)
        if (combinedLevel == 0) return 0.0

        return combinedLevel * 0.5 + 0.5
    }

    fun getWitchMagicDamageReduction() = 0.85

    private fun getCombinedMeleeEnchantLevel(item: ItemStack): Int {
        var combinedLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL)
        combinedLevel += item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)
        combinedLevel += item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)
        combinedLevel += item.getEnchantmentLevel(Enchantment.IMPALING)

        return combinedLevel
    }

    private fun getTypeBasedEnchantmentDamage(item: ItemStack, damagedEntity: LivingEntity): Double {
        var damage = 0.0

        val sharpnessLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL)
        if (sharpnessLevel > 0) {
            damage += sharpnessLevel * 0.5 + 0.5
        }

        return when (damagedEntity.category) {
            EntityCategory.NONE -> damage
            EntityCategory.ILLAGER -> damage

            EntityCategory.UNDEAD -> {
                val smiteLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)
                damage + smiteLevel * 2.5
            }

            EntityCategory.ARTHROPOD -> {
                val baneLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)
                damage + baneLevel * 2.5
            }

            EntityCategory.WATER -> {
                val impalingLevel = item.getEnchantmentLevel(Enchantment.IMPALING)
                damage + impalingLevel * 2.5
            }
        }
    }

    private fun getTotalEquipmentEnchantmentLevel(entity: LivingEntity, enchantment: Enchantment): Int {
        val equipment = entity.equipment ?: return 0

        var level = equipment.helmet?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.chestplate?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.leggings?.getEnchantmentLevel(enchantment) ?: 0
        level += equipment.boots?.getEnchantmentLevel(enchantment) ?: 0

        return level
    }
}