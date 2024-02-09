package de.fuballer.mcendgame.component.artifact.artifacts.additional_arrows

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.getAddedBaseDamage
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.setAddedBaseDamage
import de.fuballer.mcendgame.util.WorldUtil
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
        if (WorldUtil.isNotDungeonWorld(player.world)) return

        val tier = player.getHighestArtifactTier(AdditionalArrowsArtifactType) ?: return
        val (additionalArrowsAmount, damagePercentage) = AdditionalArrowsArtifactType.getValues(tier)

        val arrow = event.projectile as Arrow
        val newAddedBaseDamage = damagePercentage * arrow.getAddedBaseDamage()!!
        val newDamage = damagePercentage * arrow.damage

        (1..(additionalArrowsAmount.toInt() / 2))
            .flatMap {
                listOf(
                    player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(it * 5.0 * PI / 180.0)),
                    player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(-it * 5.0 * PI / 180.0))
                )
            }.forEach {
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