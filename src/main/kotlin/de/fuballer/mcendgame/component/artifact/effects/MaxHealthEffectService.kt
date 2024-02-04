package de.fuballer.mcendgame.component.artifact.effects

import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

@Component
class MaxHealthEffectService : Listener {
    @EventHandler
    fun on(event: PlayerJoinEvent) {
        if (WorldUtil.isDungeonWorld(event.player.world)) {
            processJoin(event.player)
        } else {
            processLeave(event.player)
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        processJoin(player)
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player
        processLeave(player)
    }

    private fun processJoin(player: Player) {
        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.MAX_HEALTH) ?: return

        val (addedHealth) = ArtifactType.MAX_HEALTH.values[tier]!!
        val realHealth = 20.0 + addedHealth

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = realHealth
        player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value!!
    }

    private fun processLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
    }
}