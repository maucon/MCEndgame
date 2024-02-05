package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.domain.CustomInventoryType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.InventoryExtension.getCustomType
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.getArtifact
import de.fuballer.mcendgame.technical.extension.PlayerExtension.setArtifacts
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
    fun on(event: InventoryClickEvent) {
        val artifactInventory = event.inventory
        if (artifactInventory.getCustomType() != CustomInventoryType.ARTIFACT) return

        event.isCancelled = true

        if (WorldUtil.isDungeonWorld(event.whoClicked.world)) {
            event.whoClicked.sendMessage(ArtifactSettings.CANNOT_CHANGE_ARTIFACTS_MESSAGE)
            return
        }

        val clickedInventory = event.clickedInventory ?: return
        val item = clickedInventory.getItem(event.slot) ?: return
        if (item.itemMeta == null) return
        if (item.getArtifact() == null) return

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return

        val player = event.whoClicked as Player

        if (event.rawSlot < ArtifactSettings.ARTIFACTS_WINDOW_SIZE) {
            removeArtifact(player.inventory, artifactInventory, item, event.slot)
        } else {
            addArtifact(player.inventory, artifactInventory, item, event.slot)
        }

        val artifacts = artifactInventory.contents
            .mapNotNull { it?.getArtifact() }

        player.setArtifacts(artifacts)
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