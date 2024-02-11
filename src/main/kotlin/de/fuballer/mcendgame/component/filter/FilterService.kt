package de.fuballer.mcendgame.component.filter

import de.fuballer.mcendgame.component.filter.db.FilterRepository
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.InventoryExtension.getCustomType
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@Component
class FilterService(
    private val filterRepo: FilterRepository
) : Listener {
    @EventHandler
    fun on(event: InventoryClickEvent) {
        val filterInventory = event.inventory
        if (filterInventory.getCustomType() != CustomInventoryType.FILTER) return

        event.isCancelled = true

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return
        val clickedInventory = event.clickedInventory ?: return
        val item = clickedInventory.getItem(event.slot) ?: return

        val player = event.whoClicked as Player
        val material = item.type
        val slot = event.rawSlot

        val entity = filterRepo.findById(player.uniqueId) ?: return

        if (slot < FilterSettings.FILTER_SIZE) {
            if (!entity.filters.remove(material)) return
            filterInventory.setItem(slot, null)
        } else {
            if (!entity.filters.add(material)) return
            filterInventory.addItem(ItemStack(material))
        }

        filterRepo.save(entity)
        filterRepo.flush()
    }

    @EventHandler
    fun on(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val item = event.item.itemStack.type
        val uuid = player.uniqueId
        val entity = filterRepo.findById(uuid) ?: return
        if (!entity.filters.contains(item)) return

        event.isCancelled = true
    }
}
