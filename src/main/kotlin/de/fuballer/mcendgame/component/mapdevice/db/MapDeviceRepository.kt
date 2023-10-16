package de.fuballer.mcendgame.component.mapdevice.db

import de.fuballer.mcendgame.component.mapdevice.MapDeviceSettings
import de.fuballer.mcendgame.db.PersistableMapRepository
import de.fuballer.mcendgame.domain.Portal
import de.fuballer.mcendgame.helper.PluginUtil
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import java.util.*

class MapDeviceRepository(
    private val plugin: Plugin
) : PersistableMapRepository<UUID, MapDeviceEntity>() {
    override fun load() {
        super.load()

        PluginUtil.scheduleSyncDelayedTask {
            this.map = findAll()
                .map {
                    val world = PluginUtil.getServer().getWorld(it.worldName) ?: return@map null

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

    fun existsMapDevicePortalByPortalEntity(entity: Entity) = findAll().flatMap { it.portals }.any { it.portalEntityId == entity.uniqueId }

    fun findByLocation(location: Location) = findAll().find { it.location == location }

    fun findByPortalEntity(entity: Entity) = findAll().find { mapDevice ->
        mapDevice.portals.any { it.portalEntityId == entity.uniqueId }
    }

    fun findMapDevicePortalByPortalEntity(entity: Entity) = findAll().flatMap { it.portals }.find { it.portalEntityId == entity.uniqueId }

    fun findByMapDevicePortalByPortalsContaining(portal: Portal) = findAll().find { it.portals.contains(portal) }

    fun getByLastClickedContains(uuid: UUID) = findAll().find { it.lastClicked.contains(uuid) }!!

    fun deleteByLocation(location: Location) {
        val entity = findByLocation(location) ?: return
        delete(entity.id)
    }
}
