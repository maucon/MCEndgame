package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.event.HandleableEvent
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import org.bukkit.potion.PotionEffect
import kotlin.math.abs

class DamageCalculationEvent(
    private val originalEvent: EntityDamageByEntityEvent,

    val damager: LivingEntity,
    val damaged: LivingEntity,
    val cause: DamageCause,
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
        val baseDamage = originalEvent.finalDamage
        val absorbedDamage = abs(originalEvent.getDamage(DamageModifier.ABSORPTION))
        val realBaseDamage = baseDamage + absorbedDamage

        return DamageUtil.calculateFinalDamage(realBaseDamage, increasedDamage, moreDamage)
    }
}