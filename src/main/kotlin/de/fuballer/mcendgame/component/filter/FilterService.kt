package de.fuballer.mcendgame.component.filter

import de.fuballer.mcendgame.component.filter.db.FilterRepository
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.InventoryExtension.getCustomType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getTotem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isCustomItemType
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@Service
class FilterService(
    private val filterRepo: FilterRepository
) : Listener {
    @EventHandler
    fun on(event: InventoryClickEvent) {
        val filterInventory = event.inventory
        if (filterInventory.getCustomType() != CustomInventoryType.FILTER) return

        event.cancel()

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return
        val clickedInventory = event.clickedInventory ?: return
        val item = clickedInventory.getItem(event.slot) ?: return

        val player = event.whoClicked as Player
        val material = item.type
        val slot = event.rawSlot

        val entity = filterRepo.findById(player.uniqueId) ?: return

        if (slot < FilterSettings.FILTER_SIZE) {
            if (!entity.filter.remove(material)) return
            filterInventory.setItem(slot, null)
        } else {
            if (!entity.filter.add(material)) return
            filterInventory.addItem(ItemStack(material))
        }

        filterRepo.save(entity)
        filterRepo.flush()
    }

    @EventHandler
    fun on(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return
        if (!event.entity.world.isDungeonWorld()) return

        val uuid = player.uniqueId
        val entity = filterRepo.findById(uuid) ?: return

        val item = event.item.itemStack
        val material = item.type

        if (item.isCustomItemType()) return
        if (item.getTotem() != null) return
        if (item.isCraftingItem()) return

        if (!entity.filter.contains(material)) return

        event.cancel()
    }
}