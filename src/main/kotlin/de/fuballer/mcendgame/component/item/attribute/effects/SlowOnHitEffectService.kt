package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

const val SLOW_RANGE = 4.0

@Service
class SlowOnHitEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val slowOnHitAttributes = event.damagerAttributes[CustomAttributeTypes.SLOW_ON_HIT] ?: return
        val slowOnHitAttribute = slowOnHitAttributes.maxOf { it.attributeRolls.getFirstAsDouble() }

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