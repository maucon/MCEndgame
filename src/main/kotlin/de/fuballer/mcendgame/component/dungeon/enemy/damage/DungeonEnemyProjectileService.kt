package de.fuballer.mcendgame.component.dungeon.enemy.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.setBaseDamage
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Creature
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class DungeonEnemyProjectileService : Listener {
    @EventHandler
    fun on(event: ProjectileLaunchEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val projectile = event.entity
        val shooter = projectile.shooter ?: return

        if (shooter !is Creature) return
        if (!shooter.isEnemy()) return

        val damage = getDamage(shooter) ?: return

        projectile.setBaseDamage(damage)
    }

    private fun getDamage(entity: Creature): Double? {
        var damage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return null

        val strengthEffect = entity.getPotionEffect(PotionEffectType.INCREASE_DAMAGE)
        val strengthEffectLevel = if (strengthEffect == null) 0 else strengthEffect.amplifier + 1
        damage -= (3 * strengthEffectLevel)

        damage *= getPowerDamageMulti(entity)
        if (strengthEffect == null) return damage

        return damage + getStrengthDamage(strengthEffect)
    }

    private fun getPowerDamageMulti(entity: Creature): Double {
        val equipment = entity.equipment ?: return 1.0
        val mainHandItem = equipment.itemInMainHand

        val powerLevel = mainHandItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)
        return DungeonEnemyDamageSettings.getPowerDamageMulti(powerLevel)
    }

    private fun getStrengthDamage(strengthEffect: PotionEffect): Double {
        return (strengthEffect.amplifier + 1) * DungeonEnemyDamageSettings.PROJECTILE_DAMAGE_PER_STRENGTH
    }
}