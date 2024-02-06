package de.fuballer.mcendgame.component.attribute

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler

@Component
class DisableMeleeEffectService {
    @EventHandler
    fun on(event: DamageCalculationEvent) {
        if (event.isProjectile) return

        if (!event.customPlayerAttributes.containsKey(AttributeType.DISABLE_MELEE)) return

        event.nullifyDamage = true
    }
}