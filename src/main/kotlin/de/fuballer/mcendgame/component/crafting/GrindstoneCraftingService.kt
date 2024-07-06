package de.fuballer.mcendgame.component.crafting

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareGrindstoneEvent
import org.bukkit.inventory.GrindstoneInventory

@Component
class GrindstoneCraftingService : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun on(event: InventoryClickEvent) {
        val inventory = event.inventory as? GrindstoneInventory ?: return
        if (inventory.type != InventoryType.GRINDSTONE) return
        if (event.rawSlot != 2) return

        val result = inventory.getItem(2) ?: return
        if (result.getCustomAttributes() == null) return
        ItemUtil.updateAttributesAndLore(result)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun on(event: PrepareGrindstoneEvent) {
        val result = event.result ?: return
        if (result.getCustomAttributes() == null) return

        val clone = result.clone()
        clone.removeEnchantments()
        ItemUtil.updateAttributesAndLore(clone)
        val cloneMeta = clone.itemMeta ?: return

        val meta = result.itemMeta ?: return
        meta.lore = cloneMeta.lore
        result.itemMeta = meta
    }
}