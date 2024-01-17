package de.fuballer.mcendgame.component.item

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareInventoryResultEvent

@Component
class CorruptedItemIntegrityService : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: PrepareInventoryResultEvent) {
        val inventory = event.inventory
        val anyCorrupted = inventory.contents.filterNotNull()
            .any { ItemUtil.isCorrupted(it) }

        if (anyCorrupted) {
            event.result = null
        }
    }

    @EventHandler
    fun on(event: CraftItemEvent) {
        val isAnyItemCorrupted = event.inventory.storageContents
            .any { ItemUtil.isCorrupted(it) }

        if (isAnyItemCorrupted) {
            event.isCancelled = true
        }
    }
}