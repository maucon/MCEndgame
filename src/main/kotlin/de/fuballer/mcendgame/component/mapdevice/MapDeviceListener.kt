package de.fuballer.mcendgame.component.mapdevice

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class MapDeviceListener(
    private val mapDeviceService: MapDeviceService
) : EventListener {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) = mapDeviceService.onBlockPlace(event)

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) = mapDeviceService.onBlockBreak(event)

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) = mapDeviceService.onPlayerInteract(event)

    @EventHandler
    fun onInventoryInteract(event: InventoryClickEvent) = mapDeviceService.onInventoryClick(event)

    @EventHandler
    fun onPlayerEntityInteract(event: PlayerInteractAtEntityEvent) = mapDeviceService.onPlayerEntityInteract(event)
}
