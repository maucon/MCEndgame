package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class TauntEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damagerCustomAttributes = event.damager.getCustomAttributes()
        val tauntAttributes = damagerCustomAttributes[AttributeType.TAUNT] ?: return

        val damaged = event.damaged as? Monster ?: return
        val tauntChance = tauntAttributes.sum()

        if (Random.nextDouble() > tauntChance) return

        damaged.target = event.damager
    }
}