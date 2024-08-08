package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Service
class ReducedDamageTakenEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val reducedDamageTakenAttributes = event.damagedAttributes[CustomAttributeTypes.REDUCED_DAMAGE_TAKEN] ?: return
        val reducedDamage = reducedDamageTakenAttributes.sumOf { it.attributeRolls.getFirstAsDouble() }

        event.reducedDamage.add(reducedDamage)
    }
}