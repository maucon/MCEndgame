package de.fuballer.mcendgame.component.artifact.effects

import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.DyeColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class WolfCompanionEffectService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.WOLF_COMPANION) ?: return

        val (count, strength) = ArtifactType.WOLF_COMPANION.values[tier]!!
        val realStrength = strength - 1

        for (i in 1..count.toInt()) {
            val targetWorld = event.locationToTeleport.world!!
            val wolf = targetWorld.spawnEntity(event.locationToTeleport, EntityType.WOLF, false) as Wolf
            wolf.owner = player

            val potionEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, Int.MAX_VALUE, realStrength.toInt(), false, false)
            wolf.addPotionEffect(potionEffect)

            wolf.isInvulnerable = true
            wolf.collarColor = DyeColor.entries.toTypedArray().random()
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        player.world.getEntitiesByClass(Wolf::class.java)
            .filter { it.owner == player }
            .forEach { it.remove() }
    }

    @EventHandler
    fun on(event: EntityTargetEvent) {
        val entity = event.entity

        if (WorldUtil.isNotDungeonWorld(entity.world)) return

        if (entity.isEnemy()) return
        if (entity !is Wolf) return

        if (event.target !is Player) return

        event.isCancelled = true
    }
}