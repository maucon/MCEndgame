package de.fuballer.mcendgame.component.map_device.db

import de.fuballer.mcendgame.domain.Portal
import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.Location
import java.util.*

data class MapDeviceEntity(
    override var id: UUID,

    val worldName: String,
    val x: Int,
    val y: Int,
    val z: Int,

    @Transient
    var location: Location,
    @Transient
    var portals: MutableList<Portal> = mutableListOf(),
    @Transient
    var lastClicked: MutableSet<UUID> = mutableSetOf()
) : Entity<UUID> {
    constructor(location: Location) : this(
        UUID.randomUUID(),
        location.world!!.name,
        location.blockX,
        location.blockY,
        location.blockZ,
        location
    )
}
