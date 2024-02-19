package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.damage.calculators.*
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import java.util.logging.Logger
import kotlin.math.abs

private val DAMAGE_TYPE_CALCULATORS = listOf(
    EntityAttackDamageCalculator,
    EntitySweepDamageCalculator,
    ProjectileDamageCalculator,
    ThornsDamageCalculator,
    MagicDamageCalculator
).associateBy { it.damageType }

@Component
class DamageService(
    private val logger: Logger
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        // debug start
        println("----- ${event.cause} to ${event.entity.type} -----")
        val oldDamageValues = DamageModifier.entries
            .filter { event.isApplicable(it) }
            .map {
                Pair(it.name.padEnd(10), event.getDamage(it))
            }
        val oldFinalValue = Pair("FINAL_DMG ", event.finalDamage)
        // debug end

        val damageTypeCalculator = DAMAGE_TYPE_CALCULATORS[event.cause] ?: DefaultDamageCalculator
        val damageEvent = damageTypeCalculator.buildDamageEvent(event)
        if (damageEvent == null) {
            logger.severe("could not map event")
            logger.severe("= event.damager ${event.damager}")
            logger.severe("= event.entity ${event.entity}")
            logger.severe("= event.cause ${event.cause}")
            return
        }

        EventGateway.apply(damageEvent)

        if (damageEvent.isCancelled) {
            event.isCancelled = true
            return
        }

        val mappedEvent = damageEvent.toBaseEvent()

        damageEvent.onHitPotionEffects.forEach {
            damageEvent.damaged.addPotionEffect(it)
        }

        // debug start
        DamageModifier.entries
            .filter { mappedEvent.isApplicable(it) }
            .withIndex()
            .forEach { (index, damageModifier) ->
                val (modifier, oldDamage) = oldDamageValues[index]
                val newDamage = mappedEvent.getDamage(damageModifier)
                val damageDiff = abs(oldDamage - newDamage)
                val chatColor = if (damageDiff > 0.001) ChatColor.RED else ChatColor.RESET
                val format = "%s%s : %.3f -> %.3f | %.3f"
                if (damageDiff > 0.001) {
                    Bukkit.getConsoleSender().sendMessage(String.format(format, chatColor, modifier, oldDamage, newDamage, damageDiff))
                }
            }
        val newDamage = mappedEvent.finalDamage
        val damageDiff = abs(oldFinalValue.second - newDamage)
        val chatColor = if (damageDiff > 0.001) ChatColor.RED else ChatColor.RESET

        val format = "%s%s : %.3f -> %.3f | %.3f"
        if (damageDiff > 0.001) {
            Bukkit.getConsoleSender().sendMessage(String.format(format, chatColor, oldFinalValue.first, oldFinalValue.second, newDamage, damageDiff))
        }
        // debug end
    }
}