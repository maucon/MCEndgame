package de.fuballer.mcendgame.component.item.attribute.effects.summon_support_wolf

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EntityExtension.getActiveSupportWolf
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.vehicle.VehicleEnterEvent

@Service
class SummonSupportWolfIntegrityService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: EntityPortalEvent) {
        val wolf = event.entity as? Wolf ?: return
        if (wolf.getActiveSupportWolf() == null) return

        event.cancel()
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: VehicleEnterEvent) {
        val wolf = event.entered as? Wolf ?: return
        if (wolf.getActiveSupportWolf() == null) return

        event.cancel()
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerInteractEntityEvent) {
        val wolf = event.rightClicked as? Wolf ?: return
        if (wolf.getActiveSupportWolf() == null) return

        event.cancel()
    }
}