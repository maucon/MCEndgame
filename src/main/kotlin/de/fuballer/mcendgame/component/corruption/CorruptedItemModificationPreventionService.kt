package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
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

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (CorruptionSettings.isAllowedInventoryType(inventory.type)) return

        val inventorySize = inventory.size
        val rawSlot = event.rawSlot

        if (rawSlot >= inventorySize) {
            if (event.action != InventoryAction.MOVE_TO_OTHER_INVENTORY) return

            val item = event.currentItem ?: return
            if (ItemUtil.isCorrupted(item)) {
                event.isCancelled = true
            }
        }

        val item = when (event.action) {
            InventoryAction.PLACE_ONE, InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME -> event.cursor
            InventoryAction.HOTBAR_SWAP ->
                if (event.hotbarButton >= 0) event.whoClicked.inventory.getItem(event.hotbarButton)
                else event.whoClicked.inventory.itemInOffHand

            else -> null
        } ?: return

        if (ItemUtil.isCorrupted(item)) {
            event.isCancelled = true
        }
    }
}