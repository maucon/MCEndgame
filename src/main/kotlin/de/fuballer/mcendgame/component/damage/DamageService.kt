package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

@Component
class DamageService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.damager.world)) return
        val player = EventUtil.getPlayerDamager(event) ?: return
        val damagedEntity = event.entity as? LivingEntity ?: return

        val baseDamage = getBaseDamage(player)

        val isCritical = isMeleeCritical(player)

        val damageEvent = DamageCalculationEvent(player, damagedEntity)
        damageEvent.baseDamage.add(baseDamage)
        damageEvent.isCritical = isCritical
        damageEvent.attackCooldown = player.attackCooldown.toDouble()
        EventGateway.apply(damageEvent)

        val calculatedDamage = calculateDamage(damageEvent)

        EntityDamageEvent.DamageModifier.entries.toTypedArray()
            .filter { event.isApplicable(it) }
            .forEach { event.setDamage(it, 0.0) }

        event.setDamage(EntityDamageEvent.DamageModifier.BASE, calculatedDamage)
    }

    private fun calculateDamage(event: DamageCalculationEvent): Double {
        var calculatedDamage = event.baseDamage.sum()
        calculatedDamage *= event.increasedDamage.sum()
        event.moreDamage.forEach { calculatedDamage *= it }

        calculatedDamage += getStrengthDamage(event.player)

        if (event.isCritical) calculatedDamage *= 1.5
        calculatedDamage *= event.attackCooldown

        return calculatedDamage
    }

    private fun getBaseDamage(player: Player): Double {
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

        /*
                The requirements for a melee critical hit are:
                - A player must be falling.
                - A player must not be on the ground.
                - A player must not be on a ladder or any type of vine.
                - A player must not be in water.
                - A player must not be affected by Blindness.
                - A player must not be affected by Slow Falling.
                - A player must not be riding an entity.
                - A player must not be faster than walking (like flying or sprinting. [Java Edition only]
                - A base attack must not be reduced below 90% damage due to cooldown. [Java Edition only]
                 */
    }
}