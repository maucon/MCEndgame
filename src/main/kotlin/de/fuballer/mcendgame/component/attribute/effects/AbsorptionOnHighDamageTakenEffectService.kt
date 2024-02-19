package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.max

@Component
class AbsorptionOnHighDamageTakenEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val absorptionAttributes = event.customDamagedAttributes[AttributeType.ABSORPTION_ON_HIGH_DAMAGE_TAKEN] ?: return
        val minDamage = absorptionAttributes.min()

        if (event.getFinalDamage() < minDamage) return

        event.onHitPotionEffects.add(PotionEffect(PotionEffectType.ABSORPTION, 100, 1, false, false, false))
    }
}