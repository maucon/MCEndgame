package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class CriticalDamageEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDamageCritical) return

        val criticalDamageAttributes = event.damagerAttributes[AttributeType.CRITICAL_DAMAGE] ?: return

        criticalDamageAttributes.forEach {
            event.moreDamage.add(it)
        }
    }
}