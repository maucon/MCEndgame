package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class SlowWhenHitEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damagerCustomAttributes = event.damager.getCustomAttributes()
        val slowOnHitAttributes = damagerCustomAttributes[AttributeType.SLOW_ON_HIT] ?: return
        val slowOnHitAttribute = slowOnHitAttributes.max()

        val duration = (slowOnHitAttribute * 20).toInt()
        val slowEffect = PotionEffect(PotionEffectType.SLOW, duration, 1, true)

        event.damaged.getNearbyEntities(4.0, 4.0, 4.0)
            .filter { it != event.damager }
            .filter { !event.isDungeonWorld || event.damaged.isEnemy() }
            .filterIsInstance<LivingEntity>()
            .forEach { it.addPotionEffect(slowEffect) }
    }
}