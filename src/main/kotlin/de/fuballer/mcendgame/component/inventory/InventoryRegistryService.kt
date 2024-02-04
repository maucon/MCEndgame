package de.fuballer.mcendgame.component.inventory

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.registry.InventoryRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

@Component
class InventoryRegistryService : Listener {
    @EventHandler
    fun on(event: InventoryCloseEvent) {
        InventoryRegistry.unregister(event.inventory)
    }
}