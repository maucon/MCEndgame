package de.fuballer.mcendgame.component.dungeon.world

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldInitEvent

@Component
class WorldConfigurationService : Listener {
    @EventHandler
    fun on(event: WorldInitEvent) {
        if (!event.world.isDungeonWorld()) return

        event.world.keepSpawnInMemory = false
    }
}