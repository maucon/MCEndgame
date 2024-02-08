package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

@Component
class DisableMeleeEffectService {
    @EventHandler
    fun on(event: DamageCalculationEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.PROJECTILE) return

        if (!event.customPlayerAttributes.containsKey(AttributeType.DISABLE_MELEE)) return

        event.nullifyDamage = true
    }
}