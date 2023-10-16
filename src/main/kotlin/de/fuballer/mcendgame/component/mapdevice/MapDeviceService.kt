package de.fuballer.mcendgame.component.mapdevice

import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationService
import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressService
import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressSettings
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.component.mapdevice.db.MapDeviceEntity
import de.fuballer.mcendgame.component.mapdevice.db.MapDeviceRepository
import de.fuballer.mcendgame.domain.Portal
import de.fuballer.mcendgame.event.DiscoverRecipeAddEvent
import de.fuballer.mcendgame.event.DungeonOpenEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.*
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import kotlin.math.max

class MapDeviceService(
    private val mapDeviceRepo: MapDeviceRepository,
    private val worldManageRepo: WorldManageRepository,
    private val dungeonGenerationService: DungeonGenerationService,
    private val playerDungeonProgressService: PlayerDungeonProgressService,
    private val plugin: Plugin,
    private val server: Server
) : Service {
    override fun initialize(plugin: Plugin) {
        createRecipe(plugin)
        clearBuggedArmorStands()
    }

    override fun terminate() {
        mapDeviceRepo.findAll()
            .forEach { closePortals(it) }
    }

    fun onBlockPlace(event: BlockPlaceEvent) {
        val placedItem = event.itemInHand
        val placedItemMeta = placedItem.itemMeta ?: return
        val placedItemLore = placedItemMeta.lore ?: return
        if (!placedItemLore.contains(MapDeviceSettings.ITEM_LORE_LINE)) return

        val block = event.block
        block.setMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY, FixedMetadataValue(plugin, MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY))

        val entity = MapDeviceEntity(block.location)
        mapDeviceRepo.save(entity)

        mapDeviceRepo.flush()
    }

    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block
        if (!block.hasMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY)) return

        block.removeMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY, plugin)

        val location = block.location
        val entity = mapDeviceRepo.findByLocation(location) ?: return

        closePortals(entity)

        mapDeviceRepo.deleteByLocation(location)
        mapDeviceRepo.flush()

        if (player.gameMode != GameMode.SURVIVAL) return

        useToolInMainhand(player)
        player.setStatistic(Statistic.MINE_BLOCK, block.type, player.getStatistic(Statistic.MINE_BLOCK, block.type) + 1)
        block.type = Material.AIR
        block.world.dropItemNaturally(block.location, MapDeviceSettings.ITEM.clone())
        event.isCancelled = true
    }

    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return
        if (block.type != Material.RESPAWN_ANCHOR) return
        if (!block.hasMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY)) return

        val player = event.player
        event.isCancelled = true
        val location = block.location

        val entity = mapDeviceRepo.findByLocation(location)
            ?: MapDeviceEntity(location)
                .apply { mapDeviceRepo.save(this) }

        player.openInventory(getMapDeviceInventory(player))

        entity.lastClicked.add(player.uniqueId)
        mapDeviceRepo.save(entity)
    }

    fun onInventoryClick(event: InventoryClickEvent) {
        if (!event.view.title.equals(MapDeviceSettings.MAP_DEVICE_INVENTORY_TITLE, ignoreCase = true)) return
        val inventory = event.inventory
        val firstSlot = inventory.getItem(0) ?: return
        val firstSlotMeta = firstSlot.itemMeta ?: return
        val firstSlotLore = firstSlotMeta.lore ?: return
        if (!firstSlotLore.contains(MapDeviceSettings.OPEN_PORTALS_ITEM_LINE)) return
        val clickedSlot = event.rawSlot

        if (clickedSlot !in 0..4) return
        event.isCancelled = true

        if (clickedSlot in 1..3) return

        handleOnInventoryClick(clickedSlot, event.whoClicked as Player)
    }

    fun onPlayerEntityInteract(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked as? ArmorStand ?: return

        val mapDevice = mapDeviceRepo.findByPortalEntity(entity) ?: return
        val portal = mapDeviceRepo.findMapDevicePortalByPortalEntity(entity) ?: return

        val player = event.player
        val dungeonWorld = portal.teleportationTargetWorld

        if (!worldManageRepo.exists(dungeonWorld.name)) {
            closePortals(mapDevice)
            player.sendMessage(MapDeviceSettings.DUNGEON_INSTANCE_CLOSED)

            return
        }

        val playerDungeonJoinEvent = PlayerDungeonJoinEvent(player, player.world, portal.teleportationTargetLocation)
        EventGateway.apply(playerDungeonJoinEvent)

        updateMapDeviceVisual(portal)
        portal.teleportPlayer(player, true)
    }

    private fun handleOnInventoryClick(
        clickedSlot: Int,
        player: Player
    ) {
        val mapDevice = mapDeviceRepo.getByLastClickedContains(player.uniqueId)
        mapDevice.lastClicked.remove(player.uniqueId)
        mapDeviceRepo.save(mapDevice)

        player.closeInventory()

        if (clickedSlot == 4) {
            closePortals(mapDevice)
            return
        }

        // slot == 0
        val mapDeviceLocation = mapDevice.location
        val mapTier = playerDungeonProgressService.getPlayerDungeonLevel(player.uniqueId).level
        val leaveLocation = mapDeviceLocation.clone().add(0.5, 1.0, 0.5)
        val teleportLocation = dungeonGenerationService.generateDungeon(mapTier, leaveLocation)

        val dungeonOpenEvent = DungeonOpenEvent(player, teleportLocation.world!!)
        EventGateway.apply(dungeonOpenEvent)

        openPortals(mapDeviceLocation, teleportLocation)
    }

    private fun clearBuggedArmorStands() {
        for (world in server.worlds) {
            clearBuggedArmorStands(world)
        }
    }

    private fun clearBuggedArmorStands(world: World) {
        for (entity in world.entities) {
            if (entity.type != EntityType.ARMOR_STAND) continue
            if (entity.name != MapDeviceSettings.MAP_DEVICE_PORTAL_ENTITY_NAME) continue
            if (mapDeviceRepo.existsMapDevicePortalByPortalEntity(entity)) continue

            entity.remove()
        }
    }

    private fun createRecipe(plugin: Plugin) {
        val key = NamespacedKey(plugin, MapDeviceSettings.MAP_DEVICE_ITEM_KEY)
        val recipe = MapDeviceSettings.getMapDeviceCraftingRecipe(key)

        val discoverRecipeAddEvent = DiscoverRecipeAddEvent(key, recipe)
        EventGateway.apply(discoverRecipeAddEvent)
    }

    private fun getMapDeviceInventory(player: Player): Inventory {
        val inventory = PluginUtil.createInventory(
            InventoryType.HOPPER,
            MapDeviceSettings.MAP_DEVICE_INVENTORY_TITLE
        )

        inventory.setItem(0, MapDeviceSettings.OPEN_PORTALS_ITEM)
        inventory.setItem(1, MapDeviceSettings.FILLER_ITEM)
        inventory.setItem(2, getDungeonLevelDisplayItem(player))
        inventory.setItem(3, MapDeviceSettings.FILLER_ITEM)
        inventory.setItem(4, MapDeviceSettings.CLOSE_PORTALS_ITEM)

        return inventory
    }

    private fun getDungeonLevelDisplayItem(player: Player): ItemStack {
        val (_, level, progress) = playerDungeonProgressService.getPlayerDungeonLevel(player.uniqueId)
        val displayItem = ItemStack(Material.BLUE_STAINED_GLASS_PANE)
        val openPortalsMeta = displayItem.itemMeta ?: return displayItem

        openPortalsMeta.setDisplayName("${ChatColor.GOLD}Level: $level")
        openPortalsMeta.lore = listOf("${ChatColor.BLUE}Progress: $progress/${PlayerDungeonProgressSettings.DUNGEON_LEVEL_INCREASE_THRESHOLD}")
        displayItem.itemMeta = openPortalsMeta

        return displayItem
    }

    private fun openPortals(mapDeviceLocation: Location, teleportLocation: Location) {
        val entity = mapDeviceRepo.findByLocation(mapDeviceLocation) ?: return
        closePortals(entity)

        val portals = mutableListOf(
            Portal(mapDeviceLocation.clone().add(2.5, 0.0, 0.5), teleportLocation, true),
            Portal(mapDeviceLocation.clone().add(-1.5, 0.0, 0.5), teleportLocation, true),
            Portal(mapDeviceLocation.clone().add(0.5, 0.0, 2.5), teleportLocation, true),
            Portal(mapDeviceLocation.clone().add(0.5, 0.0, -1.5), teleportLocation, true)
        )

        entity.portals = portals
        mapDeviceRepo.save(entity)

        val world = mapDeviceLocation.world!!
        clearBuggedArmorStands(world)

        updateMapDeviceVisual(mapDeviceLocation, 4)
    }

    private fun closePortals(entity: MapDeviceEntity) {
        entity.portals.forEach { it.close() }

        entity.portals.clear()
        mapDeviceRepo.save(entity)

        updateMapDeviceVisual(entity.location, 0)
    }

    private fun updateMapDeviceVisual(mapDeviceLocation: Location, openPortalCount: Int) {
        val block = mapDeviceLocation.block
        val anchor = block.blockData as RespawnAnchor
        anchor.charges = openPortalCount
        block.blockData = anchor
    }

    private fun updateMapDeviceVisual(portal: Portal) {
        val entity = mapDeviceRepo.findByMapDevicePortalByPortalsContaining(portal) ?: return

        val block = entity.location.block
        val anchor = block.blockData as RespawnAnchor

        anchor.charges = max(0, anchor.charges - 1)
        block.blockData = anchor
    }

    private fun useToolInMainhand(player: Player) {
        if (player.gameMode == GameMode.CREATIVE) return
        val equipment = player.equipment ?: return
        val tool = equipment.itemInMainHand
        val toolMeta = tool.itemMeta as? Damageable ?: return
        val toolType = tool.type

        player.setStatistic(Statistic.USE_ITEM, toolType, player.getStatistic(Statistic.USE_ITEM, toolType) + 1)
        if (toolMeta.hasEnchant(Enchantment.DURABILITY)) {
            val damageProbability = 1.0 / (toolMeta.getEnchantLevel(Enchantment.DURABILITY) + 1)
            if (Math.random() < damageProbability) return
        }

        if (toolMeta.damage + 1 > tool.type.maxDurability) {
            equipment.setItemInMainHand(null)
            player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f)
            player.setStatistic(Statistic.BREAK_ITEM, toolType, player.getStatistic(Statistic.BREAK_ITEM, toolType) + 1)
        }

        toolMeta.damage += 1
        tool.itemMeta = toolMeta
    }
}
