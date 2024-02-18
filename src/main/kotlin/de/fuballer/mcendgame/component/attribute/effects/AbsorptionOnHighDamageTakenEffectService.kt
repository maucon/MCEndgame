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

        //TODO if big (life only) hit

        val absorptionAttributes = event.customDamagedAttributes[AttributeType.ABSORPTION_ON_HIGH_DAMAGE_TAKEN] ?: return
        val timeInSec = absorptionAttributes.max()
        val timeInTicks = max((timeInSec * 20).toInt(), 1)

        event.onHitPotionEffects.add(PotionEffect(PotionEffectType.ABSORPTION, timeInTicks, 1, false, false, false))
    }
}