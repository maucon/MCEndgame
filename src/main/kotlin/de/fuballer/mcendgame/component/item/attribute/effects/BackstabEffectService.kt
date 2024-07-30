package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.MathUtil
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class BackstabEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val backstabAttributes = event.damagerAttributes[CustomAttributeTypes.BACKSTAB] ?: return

        val angle = MathUtil.getFacingAngle(event.damaged, event.damager)
        if (angle < 90) return

        backstabAttributes.forEach {
            val moreDamage = it.attributeRolls.getFirstAsDouble()
            event.moreDamage.add(moreDamage)
        }
    }
}