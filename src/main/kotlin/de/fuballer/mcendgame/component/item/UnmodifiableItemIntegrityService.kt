package de.fuballer.mcendgame.component.item

import de.fuballer.mcendgame.component.technical.extension.ItemStackExtension.getCorruptionRounds
import de.fuballer.mcendgame.component.technical.extension.ItemStackExtension.isUnmodifiable
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareInventoryResultEvent
import org.bukkit.inventory.Inventory

@Component
class UnmodifiableItemIntegrityService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: PrepareInventoryResultEvent) {
        val inventory = event.inventory

        if (event.eventName == PrepareAnvilEvent::class.simpleName
            && isCorruptionValid(inventory)
        ) return

        val anyUnmodifiable = inventory.contents.filterNotNull()
            .any { it.isUnmodifiable() }

        if (anyUnmodifiable) {
            event.result = null
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: CraftItemEvent) {
        val inventory = event.inventory
        val unmodifiableItemCount = inventory.storageContents
            .count { it.isUnmodifiable() }

        val result = inventory.result ?: return
        val unmodifiableItemThreshold = if (result.isUnmodifiable()) 1 else 0

        if (unmodifiableItemCount > unmodifiableItemThreshold) {
            inventory.result = null
            event.isCancelled = true
        }
    }

    private fun isCorruptionValid(inventory: Inventory): Boolean {
        val base = inventory.getItem(0) ?: return false
        val corruption = inventory.getItem(1) ?: return false

        if (base.isUnmodifiable()) return false
        return corruption.getCorruptionRounds() != null
    }
}