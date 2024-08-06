package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Service
class RegenerationOnDamageTakenEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val regenAttribute = event.damagedAttributes[CustomAttributeTypes.REGEN_ON_DAMAGE_TAKEN] ?: return

        val maxTime = regenAttribute.maxOf { it.attributeRolls.getFirstAsDouble() }
        val regenEffect = PotionEffect(PotionEffectType.REGENERATION, (maxTime * 20).toInt(), 1, false, false, false)

        event.onHitPotionEffects.add(regenEffect)
    }
}