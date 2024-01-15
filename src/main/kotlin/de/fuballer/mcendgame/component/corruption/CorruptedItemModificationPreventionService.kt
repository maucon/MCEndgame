package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryDragEvent

@Component
class CorruptedItemModificationPreventionService : Listener {
    @EventHandler
    fun onCrafting(event: CraftItemEvent) {
        val isAnyItemCorrupted = event.inventory.storageContents
            .any { ItemUtil.isCorrupted(it) }

        if (isAnyItemCorrupted) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val item = event.oldCursor
        if (!ItemUtil.isCorrupted(item)) return

        val inventory = event.inventory
        if (CorruptionSettings.isAllowedInventoryType(inventory.type)) return

        val isAnySlotNotInInventory = event.rawSlots
            .any { it < inventory.size }

        if (isAnySlotNotInInventory) {
            event.isCancelled = true
        }
    }
}