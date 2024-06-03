package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import java.util.logging.Logger
import kotlin.math.abs

private const val ALWAYS_SHOW_DEBUG = true

@Component
class DamageService(
    private val logger: Logger
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        // region debug
        println("----- ${event.cause} to ${event.entity.type} -----")
        val oldDamageValues = DamageModifier.entries
            .filter { event.isApplicable(it) }
            .map {
                Pair(it.name.padEnd(10), event.getDamage(it))
            }
        val oldFinalValue = Pair("FINAL_DMG ", event.finalDamage)
        // endregion debug

        val damageEvent = createDamageEvent(event)
        // region debug
        if (damageEvent == null) {
            logger.severe("could not map event")
            logger.severe("= damager ${event.damager}")
            logger.severe("= damaged ${event.entity}")
            logger.severe("= cause ${event.cause}")
            return
        }
        // endregion

        EventGateway.apply(damageEvent)

        if (damageEvent.isCancelled) {
            event.cancel()
            return
        }

        updateOriginalEvent(event, damageEvent)
        val target = damageEvent.damaged

        if (damageEvent.isExecute) {
            target.damage(99999.0)
        }
        damageEvent.onHitPotionEffects.forEach {
            target.addPotionEffect(it)
        }

        // region debug
        DamageModifier.entries
            .filter { event.isApplicable(it) }
            .withIndex()
            .forEach { (index, damageModifier) ->
                val (modifier, oldDamage) = oldDamageValues[index]
                val newDamage = event.getDamage(damageModifier)
                val damageDiff = abs(oldDamage - newDamage)
                val chatColor = if (damageDiff > 0.001) ChatColor.RED else ChatColor.RESET
                val format = "%s%s : %.3f -> %.3f | %.3f"
                if (damageDiff > 0.001 || ALWAYS_SHOW_DEBUG) {
                    Bukkit.getConsoleSender().sendMessage(String.format(format, chatColor, modifier, oldDamage, newDamage, damageDiff))
                }
            }
        val newDamage = event.finalDamage
        val damageDiff = abs(oldFinalValue.second - newDamage)
        val chatColor = if (damageDiff > 0.001) ChatColor.RED else ChatColor.RESET

        val format = "%s%s : %.3f -> %.3f | %.3f"
        if (damageDiff > 0.001 || ALWAYS_SHOW_DEBUG) {
            Bukkit.getConsoleSender().sendMessage(String.format(format, chatColor, oldFinalValue.first, oldFinalValue.second, newDamage, damageDiff))
        }
        // endregion debug
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
        val damagedEntity = event.entity as? LivingEntity ?: return null
        val cause = event.cause
        val isDungeonWorld = event.damager.world.isDungeonWorld()

        val isDamageBlocked = event.getDamage(DamageModifier.BLOCKING) < 0
        val isCritical = DamageUtil.isCritical(event.cause, event.damager)

        return DamageCalculationEvent(event, damager, damagedEntity, cause, isDungeonWorld, isDamageBlocked, isCritical)
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