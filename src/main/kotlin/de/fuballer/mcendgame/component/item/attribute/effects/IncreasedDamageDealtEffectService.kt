package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Service
class IncreasedDamageDealtEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val increasedDamageDealtAttributes = event.damagedAttributes[CustomAttributeTypes.INCREASED_DAMAGE] ?: return
        event.increasedDamage.add(increasedDamageDealtAttributes.sumOf { it.attributeRolls.getFirstAsDouble() })
    }
}