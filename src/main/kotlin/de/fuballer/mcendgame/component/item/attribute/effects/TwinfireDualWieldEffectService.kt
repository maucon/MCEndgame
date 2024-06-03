package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class TwinfireDualWieldEffectService : Listener{
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val values = event.damagerAttributes[AttributeType.TWINFIRE_DUAL_WIELD] ?: return
        if (values.size < 2) return

        event.moreDamage.addAll(values)
    }
}