package de.fuballer.mcendgame.component.filter

import de.fuballer.mcendgame.component.filter.db.FilterRepository
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@Service
class FilterService(
    private val filterRepo: FilterRepository
) {
    fun onInventoryClick(event: InventoryClickEvent) {
        if (!event.view.title.equals(FilterSettings.FILTER_WINDOW_TITLE, ignoreCase = true)) return
        event.isCancelled = true

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return
        val clickedInventory = event.clickedInventory ?: return
        val item = clickedInventory.getItem(event.slot) ?: return

        val inventory = event.inventory
        val player = event.whoClicked as Player
        val material = item.type
        val slot = event.rawSlot

        val entity = filterRepo.findById(player.uniqueId) ?: return

        if (slot < FilterSettings.FILTER_SIZE) {
            if (!entity.filters.remove(material)) return
            inventory.setItem(slot, null)
        } else {
            if (!entity.filters.add(material)) return
            inventory.addItem(ItemStack(material))
        }

        filterRepo.save(entity)
        filterRepo.flush()
    }

    fun onEntityItemPickup(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val item = event.item.itemStack.type
        val uuid = player.uniqueId
        val entity = filterRepo.findById(uuid) ?: return
        if (!entity.filters.contains(item)) return

        event.isCancelled = true
    }
}
