package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class CriticalExecuteEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDamageCritical) return

        val criticalExecuteAttributes = event.damagerAttributes[CustomAttributeTypes.CRITICAL_EXECUTE] ?: return
        val highestValue = criticalExecuteAttributes
            .maxOf { it.attributeRolls.getFirstAsDouble() }

        val damaged = event.damaged
        val damagedHealth = damaged.health
        val damagedMaxHealth = damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value!!
        val damagedHealthPercent = damagedHealth / damagedMaxHealth

        if (damagedHealthPercent > highestValue) return

        event.isExecute = true
    }
}