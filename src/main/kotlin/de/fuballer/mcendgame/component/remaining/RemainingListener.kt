package de.fuballer.mcendgame.component.remaining

import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

@Service
class RemainingListener(
    private val remainingService: RemainingService
) : EventListener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = remainingService.onEntityDeath(event)

    @EventHandler
    fun onDungeonComplete(event: DungeonCompleteEvent) = remainingService.onDungeonComplete(event)

    @EventHandler
    fun onDungeonWorldDelete(event: DungeonWorldDeleteEvent) = remainingService.onDungeonWorldDelete(event)
}
