package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

@Component
class EventMapper {
    @EventHandler
    fun on(event: EntityDeathEvent) {
        val entity = event.entity
        val world = entity.world
        if (WorldUtil.isNotDungeonWorld(world)) return

        val mappedEvent = DungeonEntityDeathEvent(event)
        EventGateway.apply(mappedEvent)
    }
}