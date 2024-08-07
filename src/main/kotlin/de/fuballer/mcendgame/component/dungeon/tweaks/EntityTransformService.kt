package de.fuballer.mcendgame.component.dungeon.tweaks

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTransformEvent

@Service
class EntityTransformService : Listener {
    @EventHandler
    fun on(event: EntityTransformEvent) {
        if (!event.entity.world.isDungeonWorld()) return

        event.cancel()
    }
}
