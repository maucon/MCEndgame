package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Component
class IncreasedDamageDealtEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val increasedDamageDealtAttributes = event.damagedAttributes[AttributeType.INCREASED_DAMAGE_DEALT] ?: return
        event.increasedDamage.add(increasedDamageDealtAttributes.sum())
    }
}