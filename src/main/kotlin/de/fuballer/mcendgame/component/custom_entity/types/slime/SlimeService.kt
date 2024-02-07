package de.fuballer.mcendgame.component.custom_entity.types.slime

import de.fuballer.mcendgame.component.technical.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
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