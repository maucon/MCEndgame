package de.fuballer.mcendgame.component.map_device

import de.fuballer.mcendgame.component.map_device.db.MapDeviceEntity
import de.fuballer.mcendgame.component.map_device.db.MapDeviceRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isMapDevice
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.plugin.java.JavaPlugin

@Component
class MapDeviceBlockService(
    private val mapDeviceRepo: MapDeviceRepository,
    private val mapDeviceService: MapDeviceService,
    private val plugin: JavaPlugin
) : Listener {
    @EventHandler
    fun on(event: BlockPlaceEvent) {
        val placedItem = event.itemInHand
        if (!placedItem.isMapDevice()) return

        val block = event.block
        val fixedMetadataValue = PluginUtil.createFixedMetadataValue(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY)
        block.setMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY, fixedMetadataValue)

        val entity = MapDeviceEntity(block.location)
        mapDeviceRepo.save(entity)

        mapDeviceRepo.flush()
    }

    @EventHandler
    fun on(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block
        if (!block.hasMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY)) return

        block.removeMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY, plugin)

        val location = block.location
        val entity = mapDeviceRepo.findByLocation(location) ?: return

        mapDeviceService.closeDungeon(entity)

        mapDeviceRepo.deleteByLocation(location)
        mapDeviceRepo.flush()

        if (player.gameMode != GameMode.SURVIVAL) return

        useToolInMainhand(player)

        player.setStatistic(Statistic.MINE_BLOCK, block.type, player.getStatistic(Statistic.MINE_BLOCK, block.type) + 1)
        block.type = Material.AIR
        block.world.dropItemNaturally(block.location, MapDeviceSettings.getMapDeviceItem())

        event.cancel()
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