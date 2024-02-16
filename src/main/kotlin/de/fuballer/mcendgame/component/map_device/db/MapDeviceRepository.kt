package de.fuballer.mcendgame.component.map_device.db

import de.fuballer.mcendgame.component.map_device.MapDeviceSettings
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.PersistentMapRepository
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.entity.Entity
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

@Component
class MapDeviceRepository(
    private val server: Server
) : PersistentMapRepository<UUID, MapDeviceEntity>() {
    override fun initialize(plugin: JavaPlugin) {
        super.initialize(plugin)

        PluginUtil.scheduleSyncDelayedTask {
            this.map = findAll()
                .map {
                    val world = server.getWorld(it.worldName) ?: return@map null

                    val location = Location(world, it.x.toDouble(), it.y.toDouble(), it.z.toDouble())
                    val mapDevice = world.getBlockAt(location)

                    if (mapDevice.type != Material.RESPAWN_ANCHOR) return@map null

                    mapDevice.setMetadata(MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY, FixedMetadataValue(plugin, MapDeviceSettings.MAP_DEVICE_BLOCK_METADATA_KEY))
                    MapDeviceEntity(location).apply { id = it.id }
                }
                .filterNotNull()
                .associateBy { it.id }
                .toMutableMap()
        }
    }

    fun findByLocation(location: Location) = findAll().find { it.location == location }

    fun findByPortalEntity(entity: Entity) = findAll().find { mapDevice ->
        mapDevice.portals.any { it.id == entity.uniqueId }
    }

    fun deleteByLocation(location: Location) {
        val entity = findByLocation(location) ?: return
        delete(entity.id)
    }
}
