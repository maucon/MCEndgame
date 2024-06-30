package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import kotlin.math.PI

@Component
class AdditionalArrowsEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityShootBowEvent) {
        val entity = event.entity as? LivingEntity ?: return

        val attributes = entity.getCustomAttributes()
        val additionalArrowsAttribute = attributes[AttributeType.ADDITIONAL_ARROWS] ?: return

        val damagePercentages = additionalArrowsAttribute.sortedDescending()
        val arrow = event.projectile as Arrow

        for ((index, damagePercentage) in damagePercentages.withIndex()) {
            val newDamage = damagePercentage * arrow.damage

            listOf(
                entity.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY((index + 1) * 5.0 * PI / 180.0)),
                entity.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(-(index + 1) * 5.0 * PI / 180.0))
            ).forEach {
                it.weapon = arrow.weapon
                it.damage = newDamage
                it.isCritical = arrow.isCritical
                it.pierceLevel = arrow.pierceLevel
                it.shooter = arrow.shooter
                it.fireTicks = arrow.fireTicks
                it.isVisualFire = arrow.isVisualFire
                it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
        }
    }
}