package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class RandomizedDamageTakenEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val randomizedDamageTakenAttributes = event.damagedAttributes[CustomAttributeTypes.RANDOMIZED_DAMAGE_TAKEN] ?: return

        randomizedDamageTakenAttributes.forEach {
            val minRoll = it.attributeRolls.getFirstAsDouble()
            val maxRoll = it.attributeRolls.getFirstAsDouble()
            val multiplier = minRoll + (maxRoll - minRoll) * Random.nextDouble() - 1

            event.moreDamage.add(multiplier)
        }
    }
}