package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

private val ABSORPTION_EFFECT = PotionEffect(PotionEffectType.ABSORPTION, 100, 1, false, false, false)

@Service
class AbsorptionOnHighDamageTakenEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val absorptionAttributes = event.damagedAttributes[CustomAttributeTypes.ABSORPTION_ON_HIGH_DAMAGE_TAKEN] ?: return

        val minDamage = absorptionAttributes
            .map { it.attributeRolls[0] as DoubleRoll }
            .minOf { it.getRoll() }

        if (event.getFinalDamage() >= minDamage) {
            event.onHitPotionEffects.add(ABSORPTION_EFFECT)
        }
    }
}