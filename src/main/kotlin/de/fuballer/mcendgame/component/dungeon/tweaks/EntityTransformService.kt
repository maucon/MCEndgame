package de.fuballer.mcendgame.component.dungeon.tweaks

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTransformEvent

@Component
class EntityTransformService : Listener {
    @EventHandler
    fun onEntityTransform(event: EntityTransformEvent) {
        if (!event.entity.world.isDungeonWorld()) return
        event.isCancelled = true
    }
}
