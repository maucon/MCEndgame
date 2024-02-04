package de.fuballer.mcendgame.component.dungeon.enemy.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class DungeonEnemyProjectileService : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val projectile = event.damager as? Projectile ?: return
        val shooter = projectile.shooter ?: return

        if (shooter !is LivingEntity) return

        if (!PersistentDataUtil.getBooleanValue(shooter, TypeKeys.IS_ENEMY)) return

        val damage = getDamage(shooter) ?: return
        event.damage = damage
    }

    private fun getDamage(entity: LivingEntity): Double? {
        val strengthEffect = entity.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)
        val strengthEffectLevel = if (strengthEffect == null) 0 else strengthEffect.amplifier + 1

        val totalDamage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return null
        var damage = totalDamage - (3 * strengthEffectLevel)

        damage *= getPowerDamageMulti(entity)
        if (strengthEffect == null) return damage

        return damage + getStrengthDamage(strengthEffect)
    }

    private fun getPowerDamageMulti(entity: LivingEntity): Double {
        val equipment = entity.equipment ?: return 1.0
        val mainHandItem = equipment.itemInMainHand

        val powerLevel = mainHandItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)
        return DungeonEnemyDamageSettings.getPowerDamageMulti(powerLevel)
    }

    private fun getStrengthDamage(strengthEffect: PotionEffect): Double {
        return (strengthEffect.amplifier + 1) * DungeonEnemyDamageSettings.PROJECTILE_DAMAGE_PER_STRENGTH
    }
}