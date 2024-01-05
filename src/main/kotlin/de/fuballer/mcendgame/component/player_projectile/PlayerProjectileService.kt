package de.fuballer.mcendgame.component.player_projectile

import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffectType

@Component
class PlayerProjectileService : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val projectile = event.damager as? Projectile ?: return
        val shooter = projectile.shooter ?: return

        if (shooter !is Player) return

        val addedDamage = getAddedDamage(shooter, event.damage) ?: return
        event.damage += addedDamage
    }

    private fun getAddedDamage(entity: LivingEntity, eventDamage: Double): Double? {
        val strengthEffect = entity.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)
        val strengthEffectLevel = if (strengthEffect == null) 0 else strengthEffect.amplifier + 1

        val playerBaseDamage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: return null
        val playerTotalDamage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return null
        val playerAddedDamage = playerTotalDamage - playerBaseDamage - (3 * strengthEffectLevel)

        val damageMulti = getPowerDamageMulti(entity)
        return playerAddedDamage * damageMulti
    }

    private fun getPowerDamageMulti(entity: LivingEntity): Double {
        val equipment = entity.equipment ?: return 1.0
        val mainHandItem = equipment.itemInMainHand

        val powerLevel = mainHandItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)

        return PlayerProjectileSettings.getPowerDamageMulti(powerLevel)
    }
}