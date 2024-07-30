package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.random.Random

@Component
class DodgeChanceEffectService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE
            && event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
            && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
        ) return

        val dodgeAttributes = event.damagedAttributes[CustomAttributeTypes.DODGE_CHANCE] ?: return
        val hitChance = dodgeAttributes
            .map { 1 - it.attributeRolls.getFirstAsDouble() }
            .reduce { a, b -> a * b }

        if (Random.nextDouble() >= hitChance) {
            event.cancel()
        }
    }
}