package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.random.Random

@Component
class DodgeChanceEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE
            && event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
            && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
        ) return

        val damagedCustomAttributes = event.damaged.getCustomAttributes()
        val dodgeAttributes = damagedCustomAttributes[AttributeType.DODGE_CHANCE] ?: return

        val dodgeChance = dodgeAttributes.sum()
        if (Random.nextDouble() > dodgeChance) return

        event.isCancelled = true
    }
}