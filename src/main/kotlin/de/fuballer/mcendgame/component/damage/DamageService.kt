package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.damage.dmg.DefaultDamageCalculator
import de.fuballer.mcendgame.component.damage.dmg.EntityAttackDamageCalculator
import de.fuballer.mcendgame.component.damage.dmg.EntitySweepDamageCalculator
import de.fuballer.mcendgame.component.damage.dmg.ProjectileDamageCalculator
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.abs

@Component
class DamageService : Listener {
    private val damageTypeCalculators = listOf(
        EntityAttackDamageCalculator,
        EntitySweepDamageCalculator,
        ProjectileDamageCalculator
    ).associateBy { it.damageType }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        // debug start
        println("---------- ${event.cause} ----------")
        val oldDamageValues = DamageModifier.entries
            .filter { event.isApplicable(it) }
            .map {
                Pair(it.name.padEnd(10), event.getDamage(it))
            }
        // debug end

        val damageEvent = DamageEventMapper.map(event) ?: return
        EventGateway.apply(damageEvent)

        rebuildOriginalEvent(damageEvent, event)

        // debug start
        DamageModifier.entries
            .filter { event.isApplicable(it) }
            .withIndex()
            .forEach { (index, damageModifier) ->
                val (modifier, oldDamage) = oldDamageValues[index]
                val newDamage = event.getDamage(damageModifier)
                val damageDiff = abs(oldDamage - newDamage)
                val chatColor = if (damageDiff > 0.001) ChatColor.RED else ChatColor.RESET

                val format = "%s%s: %.3f -> %.3f | %.3f"
                Bukkit.getConsoleSender().sendMessage(String.format(format, chatColor, modifier, oldDamage, newDamage, damageDiff))
            }
        // debug end
    }

    private fun rebuildOriginalEvent(damageEvent: DamageCalculationEvent, event: EntityDamageByEntityEvent) {
        val damageTypeCalculator = damageTypeCalculators[event.cause] ?: DefaultDamageCalculator

        val baseDamage = damageTypeCalculator.getBaseDamage(damageEvent)
            .let { DamageCalculation.invulnerabilityDamage(damageEvent.damaged, it) }
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
    }
}