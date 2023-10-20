package de.fuballer.mcendgame.component.dungeon.world

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldInitEvent

@Component
class WorldGenerationService : Listener {
    @EventHandler
    fun onWorldInit(event: WorldInitEvent) {
        if (WorldUtil.isNotDungeonWorld(event.world)) return

        event.world.keepSpawnInMemory = false
    }
}
