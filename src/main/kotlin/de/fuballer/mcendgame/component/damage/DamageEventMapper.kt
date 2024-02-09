package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RolledAttribute
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.getBaseDamage
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

object DamageEventMapper {
    fun map(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val isDungeonWorld = WorldUtil.isDungeonWorld(event.damager.world)
        val player = EventUtil.getPlayerDamager(event) ?: return null
        val damagedEntity = event.entity as? LivingEntity ?: return null

        val isProjectile = event.cause == EntityDamageEvent.DamageCause.PROJECTILE

        val isCritical = isCritical(isProjectile, event.damager, player)
        val baseDamage = if (isProjectile) getProjectileBaseDamage(event.damager as Projectile, event) else getMeleeBaseDamage(player)
        val enchantDamage = if (isProjectile) 0.0 else getMeleeEnchantDamage(player, damagedEntity, isDungeonWorld)

        val customPlayerAttributes = getCustomPlayerAttributes(player)
        val damageEvent = DamageCalculationEvent(player, customPlayerAttributes, damagedEntity, event.cause)
        damageEvent.baseDamage.add(baseDamage)
        damageEvent.enchantDamage = enchantDamage
        damageEvent.isCritical = isCritical
        damageEvent.attackCooldown = player.attackCooldown.toDouble()

        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            val mainHandItem = player.equipment!!.getItem(EquipmentSlot.HAND)
            damageEvent.sweepingEdgeMultiplier = getSweepingEdgeMultiplier(mainHandItem)
        }

        return damageEvent
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