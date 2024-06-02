package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

private const val minRoll = 0.25 // should be equal to attribute type description

@Component
class RandomizedDamageTakenEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damagerCustomAttributes = event.damager.getCustomAttributes()
        val randomizedDamageTakenAttributes = damagerCustomAttributes[AttributeType.RANDOMIZED_DAMAGE_TAKEN] ?: return

        randomizedDamageTakenAttributes.forEach {
            val multiplier = minRoll + (it - minRoll) * Random.nextDouble()
            event.moreDamage.add(multiplier)
        }
    }
}