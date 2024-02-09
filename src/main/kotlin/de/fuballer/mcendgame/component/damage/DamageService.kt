package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.damage.calculators.DefaultDamageCalculator
import de.fuballer.mcendgame.component.damage.calculators.EntityAttackDamageCalculator
import de.fuballer.mcendgame.component.damage.calculators.EntitySweepDamageCalculator
import de.fuballer.mcendgame.component.damage.calculators.ProjectileDamageCalculator
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.abs

private val DAMAGE_TYPE_CALCULATORS = listOf(
    EntityAttackDamageCalculator,
    EntitySweepDamageCalculator,
    ProjectileDamageCalculator
).associateBy { it.damageType }

@Component
class DamageService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        // debug start
        println("---------- ${event.cause} ----------")
        val oldDamageValues = DamageModifier.entries
            .filter { event.isApplicable(it) }
            .map {
                Pair(it.name.padEnd(10), event.getDamage(it))
            }.toMutableList()
        oldDamageValues.add(Pair("FINAL_DMG ", event.finalDamage))
        // debug end

        val damageTypeCalculator = DAMAGE_TYPE_CALCULATORS[event.cause] ?: DefaultDamageCalculator
        val damageEvent = damageTypeCalculator.buildDamageEvent(event) ?: return

        EventGateway.apply(damageEvent)

        val baseDamage = damageTypeCalculator.getBaseDamage(damageEvent)
            .let { DamageUtil.invulnerabilityDamage(damageEvent.damaged, it) }
        var leftDamage = baseDamage

        event.setDamage(DamageModifier.BASE, baseDamage)

        DamageModifier.entries
            .filter { it != DamageModifier.BASE }
            .filter { event.isApplicable(it) }
            .forEach {
                val reduction = damageTypeCalculator.getDamageReduction(damageEvent, leftDamage, it)
                event.setDamage(it, -reduction)

                leftDamage -= reduction
            }

        // debug start
        DamageModifier.entries
            .filter { event.isApplicable(it) }
            .withIndex()
            .forEach { (index, damageModifier) ->
                val (modifier, oldDamage) = oldDamageValues[index]
                val newDamage = event.getDamage(damageModifier)
                val damageDiff = abs(oldDamage - newDamage)
                val chatColor = if (damageDiff > 0.001) ChatColor.RED else ChatColor.RESET

                val format = "%s%s : %.3f -> %.3f | %.3f"
                Bukkit.getConsoleSender().sendMessage(String.format(format, chatColor, modifier, oldDamage, newDamage, damageDiff))
            }
        val (modifier, oldDamage) = oldDamageValues.last()
        val newDamage = event.finalDamage
        val damageDiff = abs(oldDamage - newDamage)
        val chatColor = if (damageDiff > 0.001) ChatColor.RED else ChatColor.RESET

        val format = "%s%s : %.3f -> %.3f | %.3f"
        Bukkit.getConsoleSender().sendMessage(String.format(format, chatColor, modifier, oldDamage, newDamage, damageDiff))
        // debug end
    }
}