package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class SlowWhenHitEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val slowOnHitAttributes = event.damagerAttributes[AttributeType.SLOW_ON_HIT] ?: return
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