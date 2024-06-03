package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.event.HandleableEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import kotlin.math.max

class DamageCalculationEvent(
    private val baseDamage: Double,

    val damager: LivingEntity,
    val damaged: LivingEntity,
    val cause: EntityDamageEvent.DamageCause,
    val isDungeonWorld: Boolean,
    val isDamageBlocked: Boolean,
    val isCritical: Boolean,

    // === custom properties ===
    val increasedDamage: MutableList<Double> = mutableListOf(),
    val moreDamage: MutableList<Double> = mutableListOf(),

    var isExecute: Boolean = false,
    val onHitPotionEffects: MutableList<PotionEffect> = mutableListOf(),
) : HandleableEvent(), Cancellable {
    private var cancelled = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    fun getFinalDamage(): Double {
        val damage = DamageUtil.calculateFinalDamage(baseDamage, increasedDamage, moreDamage)
        return max(damage, 0.0)
    }
}