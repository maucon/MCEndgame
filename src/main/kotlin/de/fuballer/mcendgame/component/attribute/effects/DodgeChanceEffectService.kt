package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

@Component
class DodgeChanceEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val dodgeAttributes = event.customDamagedAttributes[AttributeType.DODGE_CHANCE] ?: return

        val dodgeChance = dodgeAttributes.sum()
        if (Random.nextDouble() > dodgeChance) return

        event.isCancelled = true
    }
}