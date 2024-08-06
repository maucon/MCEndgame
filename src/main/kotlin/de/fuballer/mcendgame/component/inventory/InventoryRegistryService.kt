package de.fuballer.mcendgame.component.inventory

import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

@Service
class InventoryRegistryService : Listener {
    @EventHandler
    fun on(event: InventoryCloseEvent) {
        InventoryRegistry.unregister(event.inventory)
    }
}