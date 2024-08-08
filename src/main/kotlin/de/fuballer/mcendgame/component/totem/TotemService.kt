package de.fuballer.mcendgame.component.totem

import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.InventoryExtension.getCustomType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getTotem
import de.fuballer.mcendgame.util.extension.PlayerExtension.setTotems
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Service
class TotemService : Listener {
    @EventHandler
    fun on(event: InventoryClickEvent) {
        val totemInventory = event.inventory
        if (totemInventory.getCustomType() != CustomInventoryType.TOTEM) return

        event.cancel()

        if (event.whoClicked.world.isDungeonWorld()) {
            event.whoClicked.sendMessage(TotemSettings.CANNOT_CHANGE_TOTEM_MESSAGE)
            return
        }

        val clickedInventory = event.clickedInventory ?: return
        val item = clickedInventory.getItem(event.slot) ?: return
        if (item.itemMeta == null) return
        if (item.getTotem() == null) return

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return

        val player = event.whoClicked as Player

        if (event.rawSlot < TotemSettings.TOTEM_WINDOW_SIZE) {
            removeTotem(player.inventory, totemInventory, item, event.slot)
        } else {
            addTotem(player.inventory, totemInventory, item, event.slot)
        }

        val totems = totemInventory.contents
            .mapNotNull { it?.getTotem() }

        player.setTotems(totems)
    }

    private fun removeTotem(
        playerInventory: Inventory,
        totemInventory: Inventory,
        clickedItem: ItemStack,
        clickedSlot: Int
    ) {
        val firstEmpty = playerInventory.firstEmpty()
        if (firstEmpty == -1) return

        totemInventory.setItem(clickedSlot, null)
        playerInventory.addItem(clickedItem)
    }

    private fun addTotem(
        playerInventory: Inventory,
        totemInventory: Inventory,
        clickedItem: ItemStack,
        clickedSlot: Int
    ) {
        val firstEmpty = totemInventory.firstEmpty()
        if (firstEmpty == -1) return

        playerInventory.setItem(clickedSlot, null)
        totemInventory.addItem(clickedItem)
    }
}