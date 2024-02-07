package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler

@Component
class TwinfireDualWieldEffectService {
    @EventHandler
    fun on(event: DamageCalculationEvent) {
        val values = event.customPlayerAttributes[AttributeType.TWINFIRE_DUAL_WIELD] ?: return
        if (values.size < 2) return

        event.moreDamage.addAll(values)
    }
}