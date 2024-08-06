package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Service
class IncreasedMeleeDamagePerMissingHealthEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
            && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
        ) return

        val incDamageAttributes = event.damagerAttributes[CustomAttributeTypes.INCREASED_MELEE_DAMAGE_PER_MISSING_HEART] ?: return

        val damager = event.damager
        val missingHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value - damager.health

        incDamageAttributes.forEach {
            val increasePerMissingHeart = it.attributeRolls.getFirstAsDouble()
            val increasedDamage = increasePerMissingHeart * (missingHealth / 2).toInt()
            event.increasedDamage.add(increasedDamage)
        }
    }
}