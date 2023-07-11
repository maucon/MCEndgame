package de.fuballer.mcendgame.component.dungeon.leave

import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerRespawnEvent

class DungeonLeaveListener(
    private val dungeonLeaveService: DungeonLeaveService
) : EventListener {
    @EventHandler
    fun onPlayerEntityInteract(event: PlayerInteractAtEntityEvent) = dungeonLeaveService.onPlayerEntityInteract(event)

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) = dungeonLeaveService.onPlayerRespawn(event)

    @EventHandler
    fun onDungeonComplete(event: DungeonCompleteEvent) = dungeonLeaveService.onDungeonComplete(event)

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = dungeonLeaveService.onEntityDeath(event)

    @EventHandler
    fun onDungeonWorldDelete(event: DungeonWorldDeleteEvent) = dungeonLeaveService.onDungeonWorldDelete(event)
}
