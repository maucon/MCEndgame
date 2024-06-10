package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExhaustionEvent
import org.bukkit.event.entity.EntityExhaustionEvent.ExhaustionReason
import org.bukkit.event.entity.EntityRegainHealthEvent
import kotlin.math.max
import kotlin.math.min

@Component
class HealthReservationEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityRegainHealthEvent) {
        val entity = event.entity as? LivingEntity ?: return

        val unreservedHealth = getUnreservedHealth(entity)

        val finalHeal = min(max(unreservedHealth - entity.health, 0.0), event.amount)
        event.amount = finalHeal

        if (finalHeal > 0) return

        event.cancel()
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityExhaustionEvent) {
        if (event.exhaustionReason != ExhaustionReason.REGEN) return

        val entity = event.entity
        val unreservedHealth = getUnreservedHealth(entity)
        if (unreservedHealth > entity.health) return

        event.cancel()
    }

    private fun getUnreservedHealth(entity: LivingEntity): Double {
        val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value

        val attributes = entity.getCustomAttributes()
        val healthReservationAttributes = attributes[AttributeType.HEALTH_RESERVATION] ?: return maxHealth
        val unreservedHealthPercentage = 1 - healthReservationAttributes.sum()

        return maxHealth * unreservedHealthPercentage
    }
}