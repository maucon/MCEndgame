package de.fuballer.mcendgame.component.dungeon.world

import de.fuballer.mcendgame.framework.stereotype.EventListener
import de.fuballer.mcendgame.helper.WorldHelper
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldInitEvent

class WorldGenerationListener : EventListener {
    @EventHandler
    fun onWorldInit(event: WorldInitEvent) {
        // should be in a service but is a minor code snippet so whatever ¯\_(ツ)_/¯
        if (WorldHelper.isNotDungeonWorld(event.world)) return

        event.world.keepSpawnInMemory = false
    }
}
