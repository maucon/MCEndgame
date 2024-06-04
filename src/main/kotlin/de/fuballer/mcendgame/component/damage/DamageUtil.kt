package de.fuballer.mcendgame.component.damage

import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
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