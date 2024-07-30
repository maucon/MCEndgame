package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class CriticalDamageEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDamageCritical) return

        val criticalDamageAttributes = event.damagerAttributes[CustomAttributeTypes.CRITICAL_DAMAGE] ?: return

        criticalDamageAttributes.forEach {
            val moreDamage = it.attributeRolls.getFirstAsDouble()
            event.moreDamage.add(moreDamage)
        }
    }
}