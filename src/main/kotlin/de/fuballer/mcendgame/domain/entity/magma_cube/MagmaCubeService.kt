package de.fuballer.mcendgame.domain.entity.magma_cube

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.SlimeSplitEvent

@Component
class MagmaCubeService : Listener {
    @EventHandler
    fun onSlimeSplit(event: SlimeSplitEvent) {
        if (!EntityUtil.isCustomEntityType(event.entity, MagmaCubeEntityType)) return
        event.isCancelled = true
    }
}