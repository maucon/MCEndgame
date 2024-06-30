package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import java.util.logging.Logger
import kotlin.math.abs

private const val EXECUTE_DAMAGE = 99999.0

@Component
class DamageService(
    private val logger: Logger
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        val damageEvent = createDamageEvent(event) ?: return
        EventGateway.apply(damageEvent)

        if (damageEvent.isCancelled) {
            event.cancel()
            return
        }

        updateOriginalEvent(event, damageEvent)
        val target = damageEvent.damaged

        if (damageEvent.isExecute) {
            target.damage(EXECUTE_DAMAGE)
        }
        damageEvent.onHitPotionEffects.forEach {
            target.addPotionEffect(it)
        }
    }

    /**
     * Builds a DamageCalculationEvent based on the provided EntityDamageByEntityEvent.
     *
     * @param event The EntityDamageByEntityEvent to build the DamageCalculationEvent from.
     * @return A DamageCalculationEvent representing the damage calculation for the given event,
     *         or null if the event cannot be processed.
     */
    private fun createDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damager = EntityUtil.getLivingEntityDamager(event.damager) ?: return null
        val damagerAttributes = damager.getCustomAttributes()

        val damaged = event.entity as? LivingEntity ?: return null
        if (damaged is ArmorStand) return null
        val damagedAttributes = damaged.getCustomAttributes()

        val cause = event.cause
        val isDungeonWorld = event.damager.world.isDungeonWorld()

        val isDamageBlocked = event.getDamage(DamageModifier.BLOCKING) < 0
        val isCritical = DamageUtil.isCritical(event.cause, event.damager)

        return DamageCalculationEvent(event, damager, damagerAttributes, damaged, damagedAttributes, cause, isDungeonWorld, isDamageBlocked, isCritical)
    }

    private fun updateOriginalEvent(originalEvent: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent) {
        val oldRawDamage = originalEvent.damage
        val oldAbsorbedDamage = abs(originalEvent.getDamage(DamageModifier.ABSORPTION))
        val oldDamage = originalEvent.finalDamage + oldAbsorbedDamage

        val addedDamage = damageEvent.getFinalDamage() - oldDamage

        val rawDamage = oldRawDamage + addedDamage
        originalEvent.setDamage(DamageModifier.BASE, rawDamage)
        originalEvent.setDamage(DamageModifier.ABSORPTION, 0.0)

        val absorbedDamage = DamageUtil.getAbsorptionDamage(damageEvent.damaged, originalEvent.finalDamage)
        originalEvent.setDamage(DamageModifier.ABSORPTION, -absorbedDamage)
    }
}