package de.fuballer.mcendgame.component.map_device

import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressService
import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressSettings
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.component.map_device.db.MapDeviceEntity
import de.fuballer.mcendgame.component.map_device.db.MapDeviceRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.InventoryUtil
import de.fuballer.mcendgame.util.extension.InventoryExtension.getCustomType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getMapDeviceAction
import de.fuballer.mcendgame.util.extension.PlayerExtension.getLastMapDevice
import de.fuballer.mcendgame.util.extension.PlayerExtension.setLastMapDevice
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Component
class MapDeviceInventoryService(
    private val mapDeviceRepo: MapDeviceRepository,
    private val mapDeviceService: MapDeviceService,
    private val playerDungeonProgressService: PlayerDungeonProgressService,
) : Listener {
    @EventHandler
    fun on(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return
        if (block.type != Material.RESPAWN_ANCHOR) return
        if (!block.hasMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY)) return

        val player = event.player
        event.isCancelled = true
        val location = block.location

        val entity = mapDeviceRepo.findByLocation(location)
            ?: MapDeviceEntity(location).apply { mapDeviceRepo.save(this) }

        val inventory = createMapDeviceInventory(player)
        player.openInventory(inventory)
        player.setLastMapDevice(entity.id)
    }

    @EventHandler
    fun on(event: InventoryClickEvent) {
        if (event.inventory.getCustomType() != CustomInventoryType.MAP_DEVICE) return
        event.isCancelled = true

        val inventory = event.inventory
        val clickedSlot = event.rawSlot
        if (clickedSlot !in 0 until inventory.size) return

        val clickedItem = inventory.getItem(clickedSlot) ?: return

        val player = event.whoClicked as Player
        val lastMapDeviceId = player.getLastMapDevice() ?: return
        val mapDevice = mapDeviceRepo.findById(lastMapDeviceId) ?: return

        val action = clickedItem.getMapDeviceAction() ?: return
        player.closeInventory()

        when (action) {
            MapDeviceAction.OPEN -> mapDeviceService.openDungeon(mapDevice, player)
            MapDeviceAction.CLOSE -> mapDeviceService.closeDungeon(mapDevice)
        }
    }

    private fun createMapDeviceInventory(player: Player): Inventory {
        val inventory = InventoryUtil.createInventory(
            InventoryType.HOPPER,
            MapDeviceSettings.MAP_DEVICE_INVENTORY_TITLE,
            CustomInventoryType.MAP_DEVICE
        )

        inventory.setItem(0, MapDeviceSettings.OPEN_PORTALS_ITEM)
        inventory.setItem(1, MapDeviceSettings.FILLER_ITEM)
        inventory.setItem(2, getDungeonTierDisplayItem(player))
        inventory.setItem(3, MapDeviceSettings.FILLER_ITEM)
        inventory.setItem(4, MapDeviceSettings.CLOSE_PORTALS_ITEM)

        return inventory
    }

    private fun getDungeonTierDisplayItem(player: Player): ItemStack {
        val (_, tier, progress) = playerDungeonProgressService.getPlayerDungeonLevel(player.uniqueId)
        val displayItem = ItemStack(Material.BLUE_STAINED_GLASS_PANE)
        val openPortalsMeta = displayItem.itemMeta ?: return displayItem

        openPortalsMeta.setDisplayName("${ChatColor.GOLD}Tier: $tier")
        openPortalsMeta.lore = listOf("${ChatColor.BLUE}Progress: $progress/${PlayerDungeonProgressSettings.DUNGEON_LEVEL_INCREASE_THRESHOLD}")
        displayItem.itemMeta = openPortalsMeta

        return displayItem
    }
}