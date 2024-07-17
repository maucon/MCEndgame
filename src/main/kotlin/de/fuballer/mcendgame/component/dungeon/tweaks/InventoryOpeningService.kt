package de.fuballer.mcendgame.component.dungeon.tweaks

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType

val OPENABLE_INVENTORIES = listOf(
    InventoryType.CRAFTING,
    InventoryType.PLAYER,
    InventoryType.CREATIVE,
    InventoryType.MERCHANT
)

@Component
class InventoryOpeningService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: InventoryOpenEvent) {
        if (!event.player.world.isDungeonWorld()) return
        if (OPENABLE_INVENTORIES.contains(event.inventory.type)) return

        event.cancel()
    }
}