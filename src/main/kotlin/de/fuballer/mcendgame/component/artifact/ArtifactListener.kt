package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

class ArtifactListener(
    private val artifactService: ArtifactService,
) : EventListener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = artifactService.onInventoryClick(event)

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) = artifactService.onInventoryDrag(event)

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = artifactService.onEntityDeath(event)
}
