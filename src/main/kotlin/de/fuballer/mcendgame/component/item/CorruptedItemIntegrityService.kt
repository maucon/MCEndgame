package de.fuballer.mcendgame.component.item

import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareInventoryResultEvent
import org.bukkit.inventory.Inventory

@Component
class CorruptedItemIntegrityService : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: PrepareInventoryResultEvent) {
        val inventory = event.inventory

        if (event.eventName == PrepareAnvilEvent::class.simpleName
            && isCorruptionValid(inventory)
        ) return

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

    private fun isCorruptionValid(inventory: Inventory): Boolean {
        val base = inventory.getItem(0) ?: return false
        val corruption = inventory.getItem(1) ?: return false
        val corruptionMeta = corruption.itemMeta ?: return false

        if (ItemUtil.isCorrupted(base)) return false
        return PersistentDataUtil.getValue(corruptionMeta, DataTypeKeys.CORRUPTION_ROUNDS) != null
    }
}