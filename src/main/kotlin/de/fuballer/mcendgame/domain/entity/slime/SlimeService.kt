package de.fuballer.mcendgame.domain.entity.slime

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.SlimeSplitEvent

@Component
class SlimeService : Listener {
    @EventHandler
    fun onSlimeSplit(event: SlimeSplitEvent) {
        if (event.entity.getCustomEntityType() != SlimeEntityType) return

        event.isCancelled = true
    }
}