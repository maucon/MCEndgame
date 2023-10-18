package de.fuballer.mcendgame.component.dungeon.progress

import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

@Component
class PlayerDungeonProgressListener(
    private val playerDungeonProgressService: PlayerDungeonProgressService
) : EventListener {
    @EventHandler
    fun onDungeonComplete(event: DungeonCompleteEvent) = playerDungeonProgressService.onDungeonComplete(event)

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = playerDungeonProgressService.onEntityDeath(event)

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) = playerDungeonProgressService.onPlayerRespawn(event)
}
