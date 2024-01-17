package de.fuballer.mcendgame.component.dungeon.tweaks.entity_transform

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTransformEvent

@Component
class EntityTransformService : Listener {
    @EventHandler
    fun onEntityTransform(event: EntityTransformEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return
        event.isCancelled = true
    }
}
