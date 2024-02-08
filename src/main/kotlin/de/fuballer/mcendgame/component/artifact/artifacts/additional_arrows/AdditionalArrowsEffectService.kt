package de.fuballer.mcendgame.component.artifact.artifacts.additional_arrows

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityShootBowEvent
import kotlin.math.PI

@Component
class AdditionalArrowsEffectService {
    @EventHandler
    fun on(event: EntityShootBowEvent) {
        val player = event.entity as? Player ?: return
        if (WorldUtil.isNotDungeonWorld(player.world)) return

        val tier = player.getHighestArtifactTier(AdditionalArrowsArtifactType) ?: return

        val additionalArrowsAmount = AdditionalArrowsArtifactType.getValues(tier)[0]
        val damagePercentage = AdditionalArrowsArtifactType.getValues(tier)[1] / 100.0

        val arrow = event.projectile as Arrow

        (1..(additionalArrowsAmount.toInt() / 2))
            .flatMap {
                listOf(
                    player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(it * 5.0 * PI / 180.0)),
                    player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(-it * 5.0 * PI / 180.0))
                )
            }.forEach {
                it.damage = arrow.damage * damagePercentage
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