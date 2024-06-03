package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class IncreasedDamagePerMissingHealthEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val incDamageAttributes = event.damagerAttributes[AttributeType.INCREASED_DAMAGE_PER_MISSING_HEART] ?: return

        val damager = event.damager
        val missingHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value - damager.health

        incDamageAttributes.forEach {
            val increasedDamage = it * (missingHealth / 2).toInt()
            event.increasedDamage.add(increasedDamage)
        }
    }
}