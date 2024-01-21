package de.fuballer.mcendgame.component.item

import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareInventoryResultEvent
import org.bukkit.inventory.Inventory

@Component
class CorruptedItemIntegrityService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: PrepareInventoryResultEvent) {
        val inventory = event.inventory

        if (event.eventName == PrepareAnvilEvent::class.simpleName
            && isCorruptionValid(inventory)
        ) return

        val anyUnmodifiable = inventory.contents.filterNotNull()
            .any { ItemUtil.isUnmodifiable(it) }

        if (anyUnmodifiable) {
            event.result = null
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: CraftItemEvent) {
        val inventory = event.inventory
        val unmodifiableItemCount = inventory.storageContents
            .count { ItemUtil.isUnmodifiable(it) }

        val result = inventory.result ?: return
        val unmodifiableItemThreshold = if (ItemUtil.isUnmodifiable(result)) 1 else 0

        if (unmodifiableItemCount > unmodifiableItemThreshold) {
            inventory.result = null
            event.isCancelled = true
        }
    }

    private fun isCorruptionValid(inventory: Inventory): Boolean {
        val base = inventory.getItem(0) ?: return false
        val corruption = inventory.getItem(1) ?: return false
        val corruptionMeta = corruption.itemMeta ?: return false

        if (ItemUtil.isUnmodifiable(base)) return false
        return PersistentDataUtil.getValue(corruptionMeta, TypeKeys.CORRUPTION_ROUNDS) != null
    }
}