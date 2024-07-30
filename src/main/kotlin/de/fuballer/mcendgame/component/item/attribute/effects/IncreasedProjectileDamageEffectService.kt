package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Component
class IncreasedProjectileDamageEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) return

        val values = event.damagerAttributes[CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE] ?: return

        values.forEach {
            val increasedDamage = it.attributeRolls.getFirstAsDouble()
            event.increasedDamage.add(increasedDamage)
        }
    }
}