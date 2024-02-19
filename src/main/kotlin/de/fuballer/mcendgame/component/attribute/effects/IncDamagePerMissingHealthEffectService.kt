package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class IncDamagePerMissingHealthEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damager = event.damager
        val damagerCustomAttributes = damager.getCustomAttributes()
        val incDamageAttributes = damagerCustomAttributes[AttributeType.INC_DAMAGE_PER_MISSING_HEALTH] ?: return

        val missingHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value - damager.health

        incDamageAttributes.forEach {
            val increasedDamage = it * (missingHealth / 2).toInt()
            event.increasedDamage.add(increasedDamage)
        }
    }
}