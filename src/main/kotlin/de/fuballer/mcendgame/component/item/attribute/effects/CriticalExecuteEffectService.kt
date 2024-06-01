package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class CriticalExecuteEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damagerCustomAttributes = event.damager.getCustomAttributes()
        val criticalExecuteAttributes = damagerCustomAttributes[AttributeType.CRITICAL_EXECUTE] ?: return

        val highestValue = criticalExecuteAttributes.max()

        val damaged = event.damaged
        val damagedHealth = damaged.health
        val damagedMaxHealth = damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: damagedHealth
        val damagedHealthPercent = damagedHealth / damagedMaxHealth

        if (damagedHealthPercent > highestValue) return

        event.isExecute = true
    }
}