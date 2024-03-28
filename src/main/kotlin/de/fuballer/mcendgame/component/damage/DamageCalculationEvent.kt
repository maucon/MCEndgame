package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.damage.calculators.DamageCauseCalculator
import de.fuballer.mcendgame.event.HandleableEvent
import org.bukkit.Difficulty
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import org.bukkit.potion.PotionEffect

class DamageCalculationEvent(
    private val originalEvent: EntityDamageByEntityEvent,
    private val damageCauseCalculator: DamageCauseCalculator,

    val damager: LivingEntity,
    val damaged: LivingEntity,
    val cause: EntityDamageEvent.DamageCause,
    val isDungeonWorld: Boolean,
    val isDamageBlocked: Boolean,
    val difficulty: Difficulty,

    val baseDamage: MutableList<Double> = mutableListOf(),
    val increasedDamage: MutableList<Double> = mutableListOf(),
    val moreDamage: MutableList<Double> = mutableListOf(),
    var enchantDamage: Double = 0.0,
    var isCritical: Boolean = false,
    var criticalRoll: Double = 0.0,
    var sweepingEdgeMultiplier: Double = 0.0,
    var onHitPotionEffects: MutableList<PotionEffect> = mutableListOf(),
) : HandleableEvent(), Cancellable {
    private var cancelled = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    fun toBaseEvent(): EntityDamageByEntityEvent {
        val baseDamage = damageCauseCalculator.getBaseDamage(this)
        var leftDamage = baseDamage

        originalEvent.setDamage(DamageModifier.BASE, baseDamage)
        DamageModifier.entries
            .filter { it != DamageModifier.BASE }
            .filter { originalEvent.isApplicable(it) }
            .forEach {
                val reduction = damageCauseCalculator.getFlatDamageReduction(this, leftDamage, it)
                originalEvent.setDamage(it, -reduction)

                leftDamage -= reduction
            }

        return originalEvent
    }

    fun getFinalDamage() = toBaseEvent().finalDamage
}