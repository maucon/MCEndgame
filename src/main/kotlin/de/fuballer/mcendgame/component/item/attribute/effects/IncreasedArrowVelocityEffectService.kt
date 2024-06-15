package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class IncreasedArrowVelocityEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: EntityShootBowEvent) {
        val entity = event.entity as? LivingEntity ?: return

        val attributes = entity.getCustomAttributes()
        val incProjVelocityAttributes = attributes[AttributeType.INCREASED_ARROW_VELOCITY] ?: return
        val projVelocityMultiplier = 1 + incProjVelocityAttributes.sum()

        val arrow = event.projectile as? Arrow ?: return
        arrow.velocity = arrow.velocity.multiply(projVelocityMultiplier)
    }
}