package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.MathUtil
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class BackstabEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damagerCustomAttributes = event.damager.getCustomAttributes()
        val backstabAttributes = damagerCustomAttributes[AttributeType.BACKSTAB] ?: return

        val angle = MathUtil.getFacingAngle(event.damaged, event.damager)
        if (angle < 90) return

        backstabAttributes.forEach {
            event.moreDamage.add(it)
        }
    }
}