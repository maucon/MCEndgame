package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Component
class ReducedProjectileDamageTakenEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) return

        val reducedProjDamageTakenAttributes = event.damagedAttributes[CustomAttributeTypes.REDUCED_PROJECTILE_DAMAGE_TAKEN] ?: return
        val reducedDamage = -reducedProjDamageTakenAttributes.sumOf { it.attributeRolls.getFirstAsDouble() }

        event.moreDamage.add(reducedDamage)
    }
}