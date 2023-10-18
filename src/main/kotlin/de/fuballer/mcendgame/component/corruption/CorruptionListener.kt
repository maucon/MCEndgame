package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.PrepareAnvilEvent

@Component
class CorruptionListener(
    private val corruptionService: CorruptionService
) : EventListener {
    @EventHandler
    fun onAnvilPrepare(event: PrepareAnvilEvent) = corruptionService.onAnvilPrepare(event)

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = corruptionService.onInventoryClick(event)

    @EventHandler
    fun onCrafting(event: CraftItemEvent) = corruptionService.onCrafting(event)

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) = corruptionService.onInventoryDrag(event)
}
