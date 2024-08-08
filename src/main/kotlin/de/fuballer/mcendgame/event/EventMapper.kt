package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

@Service
class EventMapper : Listener {
    @EventHandler
    fun on(event: EntityDeathEvent) {
        val entity = event.entity
        val world = entity.world
        if (!world.isDungeonWorld()) return

        val mappedEvent = DungeonEntityDeathEvent(event.entity, event.drops)
        EventGateway.apply(mappedEvent)

        event.drops.clear()
        event.drops.addAll(mappedEvent.drops)
    }
}