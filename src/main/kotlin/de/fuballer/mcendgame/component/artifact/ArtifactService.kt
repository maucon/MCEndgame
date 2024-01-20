package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Component
class ArtifactService : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (!event.view.title.contains(ArtifactSettings.ARTIFACTS_WINDOW_TITLE, true)) return
        event.isCancelled = true

        if (WorldUtil.isDungeonWorld(event.whoClicked.world)) {
            event.whoClicked.sendMessage(ArtifactSettings.CANNOT_CHANGE_ARTIFACTS_MESSAGE)
            return
        }

        val clickedInventory = event.clickedInventory ?: return
        val item = clickedInventory.getItem(event.slot) ?: return
        if (!ArtifactUtil.isArtifact(item)) return

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return

        val artifactInventory = event.inventory
        val player = event.whoClicked as Player

        if (event.rawSlot < ArtifactSettings.ARTIFACTS_WINDOW_SIZE) {
            removeArtifact(player.inventory, artifactInventory, item, event.slot)
        } else {
            addArtifact(player.inventory, artifactInventory, item, event.slot)
        }

        val artifacts = artifactInventory.contents
            .filterNotNull()
            .mapNotNull { ArtifactUtil.fromItem(it) }

        PersistentDataUtil.setValue(player, TypeKeys.ARTIFACTS, artifacts)
    }

    private fun removeArtifact(
        playerInventory: Inventory,
        artifactInventory: Inventory,
        clickedItem: ItemStack,
        clickedSlot: Int
    ) {
        val firstEmpty = playerInventory.firstEmpty()
        if (firstEmpty == -1) return

        artifactInventory.setItem(clickedSlot, null)
        playerInventory.addItem(clickedItem)
    }

    private fun addArtifact(
        playerInventory: Inventory,
        artifactInventory: Inventory,
        clickedItem: ItemStack,
        clickedSlot: Int
    ) {
        val firstEmpty = artifactInventory.firstEmpty()
        if (firstEmpty == -1) return

        playerInventory.setItem(clickedSlot, null)
        artifactInventory.addItem(clickedItem)
    }
}