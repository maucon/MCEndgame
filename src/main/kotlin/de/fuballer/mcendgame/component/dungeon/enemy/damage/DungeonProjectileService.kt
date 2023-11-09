package de.fuballer.mcendgame.component.dungeon.enemy.damage

import de.fuballer.mcendgame.component.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffectType

@Component
class DungeonProjectileService : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val projectile = event.damager as? Projectile ?: return
        val shooter = projectile.shooter ?: return

        if (shooter !is LivingEntity) return

        if (PersistentDataUtil.getValue(shooter, DataTypeKeys.ENTITY_TYPE) == null) return

        val damage = getDamage(shooter) ?: return
        event.damage = damage
    }

    private fun getDamage(entity: LivingEntity): Double? {
        var damage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return null

        damage += getStrengthDamage(entity)
        damage *= getPowerDamageMulti(entity)

        return damage
    }

    private fun getStrengthDamage(entity: LivingEntity): Double {
        val strengthEffect = entity.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) ?: return 0.0
        return (strengthEffect.amplifier + 1) * DungeonEnemyDamageSettings.DAMAGE_PER_STRENGTH
    }

    private fun getPowerDamageMulti(entity: LivingEntity): Double {
        val equipment = entity.equipment ?: return 1.0
        val mainHandItem = equipment.itemInMainHand

        val powerLevel = mainHandItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)

        return DungeonEnemyDamageSettings.getPowerDamageMulti(powerLevel)
    }
}