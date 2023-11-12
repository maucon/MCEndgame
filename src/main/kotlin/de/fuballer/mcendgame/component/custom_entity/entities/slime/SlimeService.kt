package de.fuballer.mcendgame.component.custom_entity.entities.slime

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.SlimeSplitEvent

@Component
class SlimeService : Listener {
    @EventHandler
    fun onSlimeSplit(event: SlimeSplitEvent) {
        if (!CustomEntityType.isType(event.entity, CustomEntityType.SLIME)) return
        event.isCancelled = true
    }
}