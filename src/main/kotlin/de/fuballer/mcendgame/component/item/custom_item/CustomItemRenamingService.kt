package de.fuballer.mcendgame.component.item.custom_item

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isCustomItemType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent

@Service
class CustomItemRenamingService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: PrepareAnvilEvent) {
        val inventory = event.inventory

        val base = inventory.getItem(0) ?: return
        if (!base.isCustomItemType()) return

        if (ItemUtil.isItemRenaming(inventory.renameText, base)) {
            event.result = null
        }
    }
}