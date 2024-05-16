package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.MathUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent

@Component
class StealthEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: EntityTargetEvent) {
        if (event.reason != EntityTargetEvent.TargetReason.CLOSEST_PLAYER
            && event.reason != EntityTargetEvent.TargetReason.CLOSEST_ENTITY
        ) return

        val target = event.target as? LivingEntity ?: return

        val damagerCustomAttributes = target.getCustomAttributes()
        val stealthAttributes = damagerCustomAttributes[AttributeType.STEALTH] ?: return

        if (stealthAttributes.isEmpty()) return

        val angle = MathUtil.getFacingAngle(event.entity, target)
        if (angle < 90) return

        event.cancel()
    }
}