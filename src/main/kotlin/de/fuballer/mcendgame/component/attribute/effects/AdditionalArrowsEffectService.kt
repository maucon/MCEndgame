package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.ProjectileExtension.getAddedBaseDamage
import de.fuballer.mcendgame.util.extension.ProjectileExtension.setAddedBaseDamage
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import kotlin.math.PI

@Component
class AdditionalArrowsEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityShootBowEvent) {
        val player = event.entity as? Player ?: return

        val attributes = player.getCustomAttributes()
        val additionalArrowsAttribute = attributes[AttributeType.ADDITIONAL_ARROWS] ?: return

        val damagePercentages = additionalArrowsAttribute.sortedDescending()

        val arrow = event.projectile as Arrow
        val addedBaseDamage = arrow.getAddedBaseDamage() ?: 0.0

        for ((index, damagePercentage) in damagePercentages.withIndex()) {
            val newAddedBaseDamage = damagePercentage * addedBaseDamage
            val newDamage = damagePercentage * arrow.damage

            listOf(
                player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY((index + 1) * 5.0 * PI / 180.0)),
                player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(-(index + 1) * 5.0 * PI / 180.0))
            ).forEach {
                it.setAddedBaseDamage(newAddedBaseDamage)
                it.damage = newDamage
                it.isCritical = arrow.isCritical
                it.isShotFromCrossbow = arrow.isShotFromCrossbow
                it.pierceLevel = arrow.pierceLevel
                it.knockbackStrength = arrow.knockbackStrength
                it.shooter = arrow.shooter
                it.fireTicks = arrow.fireTicks
                it.isVisualFire = arrow.isVisualFire
                it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
        }
    }
}