package de.fuballer.mcendgame.component.dungeon.tweaks

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

val DISABLED_BLOCK_TYPES = listOf(
    Material.CHEST,
    Material.CHEST_MINECART,
    Material.ENDER_CHEST,
    Material.BARREL,
    Material.FURNACE,
    Material.BLAST_FURNACE,
    Material.SMOKER,
    Material.FURNACE_MINECART,
    Material.CRAFTING_TABLE,
    Material.CARTOGRAPHY_TABLE,
    Material.ENCHANTING_TABLE,
    Material.FLETCHING_TABLE,
    Material.SMITHING_TABLE,
    Material.CHISELED_BOOKSHELF,
    Material.DISPENSER,
    Material.DROPPER,
    Material.BREWING_STAND,
    Material.ANVIL,
    Material.BEACON,
    Material.HOPPER,
    Material.LOOM,
    Material.GRINDSTONE,
    Material.CRAFTER
)

@Service
class InventoryBlockInteractionService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerInteractEvent) {
        if (!event.player.world.isDungeonWorld()) return
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val blockType = event.clickedBlock?.type
        if (DISABLED_BLOCK_TYPES.contains(blockType)) {
            event.cancel()
        }
    }
}