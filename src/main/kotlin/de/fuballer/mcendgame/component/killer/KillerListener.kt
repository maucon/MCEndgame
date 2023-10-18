package de.fuballer.mcendgame.component.killer

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent

@Service
class KillerListener(
    private val killerService: KillerService
) : EventListener {
    @EventHandler
    fun onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent) = killerService.onEntityDamageByEntityEvent(event)

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = killerService.onInventoryClick(event)
}
