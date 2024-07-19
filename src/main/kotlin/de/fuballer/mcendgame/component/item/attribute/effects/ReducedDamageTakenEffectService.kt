package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Component
class ReducedDamageTakenEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val reducedDamageTakenAttributes = event.damagedAttributes[AttributeType.REDUCED_DAMAGE_TAKEN] ?: return
        val reducedDamage = -reducedDamageTakenAttributes.sum()

        event.moreDamage.add(reducedDamage)
    }
}