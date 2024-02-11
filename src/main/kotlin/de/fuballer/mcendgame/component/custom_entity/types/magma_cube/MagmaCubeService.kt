package de.fuballer.mcendgame.component.custom_entity.types.magma_cube

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.SlimeSplitEvent

@Component
class MagmaCubeService : Listener {
    @EventHandler
    fun onSlimeSplit(event: SlimeSplitEvent) {
        if (event.entity.getCustomEntityType() != MagmaCubeEntityType) return

        event.isCancelled = true
    }
}