package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.event.EntityHealEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
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

@Service
class HealthReservationEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityRegainHealthEvent) {
        val entity = event.entity as? LivingEntity ?: return
        val finalHeal = getFinalHealAmount(entity, event.amount)

        event.amount = finalHeal

        if (finalHeal <= 0) {
            event.cancel()
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityHealEvent) {
        val finalHeal = getFinalHealAmount(event.entity, event.amount)
        event.entity.health += finalHeal
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityExhaustionEvent) {
        if (event.exhaustionReason != ExhaustionReason.REGEN) return

        val entity = event.entity
        val unreservedHealth = getUnreservedHealth(entity)
        if (unreservedHealth > entity.health + 0.01) return // +0.01 prevents inaccuracy

        event.cancel()
    }

    private fun getFinalHealAmount(entity: LivingEntity, amount: Double): Double {
        val unreservedHealth = getUnreservedHealth(entity)
        return min(max(unreservedHealth - entity.health, 0.0), amount)
    }

    private fun getUnreservedHealth(entity: LivingEntity): Double {
        val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value

        val attributes = entity.getCustomAttributes()
        val healthReservationAttributes = attributes[CustomAttributeTypes.HEALTH_RESERVATION] ?: return maxHealth
        val unreservedHealthPercentage = 1 - healthReservationAttributes.sumOf { it.attributeRolls.getFirstAsDouble() }

        return maxHealth * unreservedHealthPercentage
    }
}