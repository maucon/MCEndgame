package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.RolledAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getRolledAttributes
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffectType
import kotlin.math.max
import kotlin.math.min

object DamageUtil {
    /** flat, increase, more */
    fun calculateFinalDamage(baseDamage: Double, increase: List<Double>, more: List<Double>): Double {
        var damage = baseDamage
        damage *= 1 + increase.sum()
        more.forEach { damage *= 1 + it }

        return damage
    }

    fun getAbsorptionDamage(entity: LivingEntity, damage: Double) =
        max(0.0, min(entity.absorptionAmount, damage))

    fun getEntityCustomAttributes(entity: LivingEntity): Map<AttributeType, List<Double>> {
        val attributes = mutableListOf<RolledAttribute>()
        val equipment = entity.equipment ?: return mapOf()

        equipment.helmet?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.chestplate?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.leggings?.getRolledAttributes()?.let { attributes.addAll(it) }
        equipment.boots?.getRolledAttributes()?.let { attributes.addAll(it) }

        val mainHandItem = equipment.itemInMainHand
        val grantAttributesInMainHand = Equipment.fromMaterial(mainHandItem.type)
            ?.let { it.slot == EquipmentSlot.HAND || !it.extraAttributesInSlot } ?: false

        if (grantAttributesInMainHand) {
            mainHandItem.getRolledAttributes()?.let { attributes.addAll(it) }
        }

        val offHandItem = equipment.itemInOffHand
        val grantAttributesInOffHand = Equipment.fromMaterial(offHandItem.type)
            ?.let { it.slot == EquipmentSlot.OFF_HAND || !it.extraAttributesInSlot } ?: false

        if (grantAttributesInOffHand) {
            offHandItem.getRolledAttributes()?.let { attributes.addAll(it) }
        }

        return attributes
            .filter { !it.type.isVanillaAttributeType }
            .groupBy { it.type }
            .mapValues { (_, values) -> values.map { it.roll } }
    }

    fun isCritical(cause: DamageCause, damager: Entity): Boolean =
        when (cause) {
            DamageCause.PROJECTILE -> isProjectileCritical(damager)
            DamageCause.ENTITY_ATTACK -> isMeleeCritical(damager)
            else -> false
        }

    private fun isProjectileCritical(entity: Entity): Boolean {
        val arrow = entity as? Arrow ?: return false
        return arrow.isCritical
    }

    private fun isMeleeCritical(entity: Entity): Boolean {
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
}