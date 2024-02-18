package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import kotlin.random.Random

@Component
class DodgeChanceEffectService {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val dodgeAttributes = event.customDamagedAttributes[AttributeType.DODGE_CHANCE] ?: return

        val dodgeChance = dodgeAttributes.sum()
        if (Random.nextDouble() > dodgeChance) return

        event.isCancelled = true
    }
}