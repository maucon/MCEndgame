package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.event.HandleableEvent
import org.bukkit.Difficulty
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect

class DamageCalculationEvent(
    val damager: LivingEntity,
    val customDamagerAttributes: Map<AttributeType, List<Double>>,
    val damaged: LivingEntity,
    val customDamagedAttributes: Map<AttributeType, List<Double>>,
    val cause: EntityDamageEvent.DamageCause,
    val isDungeonWorld: Boolean,
    val damageBlocked: Boolean,
    val difficulty: Difficulty,

    val baseDamage: MutableList<Double> = mutableListOf(),
    val increasedDamage: MutableList<Double> = mutableListOf(),
    val moreDamage: MutableList<Double> = mutableListOf(),
    var enchantDamage: Double = 0.0,
    var isCritical: Boolean = false,
    var criticalRoll: Double = 0.0,
    var sweepingEdgeMultiplier: Double = 0.0,
    var onHitPotionEffects: MutableList<PotionEffect> = mutableListOf()
) : HandleableEvent(), Cancellable {
    private var cancelled = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }
}