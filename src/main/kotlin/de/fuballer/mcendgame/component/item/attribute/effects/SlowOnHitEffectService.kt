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

const val SLOW_RANGE = 4.0

@Component
class SlowOnHitEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val slowOnHitAttributes = event.damagerAttributes[AttributeType.SLOW_ON_HIT] ?: return
        val slowOnHitAttribute = slowOnHitAttributes.max()

        val duration = (slowOnHitAttribute * 20).toInt()
        val slowEffect = PotionEffect(PotionEffectType.SLOWNESS, duration, 1, true)

        val damaged = event.damaged

        damaged.addPotionEffect(slowEffect)
        damaged.getNearbyEntities(SLOW_RANGE, SLOW_RANGE, SLOW_RANGE)
            .filter { it != event.damager }
            .filter { !event.isDungeonWorld || it.isEnemy() }
            .filterIsInstance<LivingEntity>()
            .forEach { it.addPotionEffect(slowEffect) }
    }
}