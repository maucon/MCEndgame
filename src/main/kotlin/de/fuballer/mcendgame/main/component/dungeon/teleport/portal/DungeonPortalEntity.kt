package de.fuballer.mcendgame.main.component.dungeon.teleport.portal

import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.maucon.mauconframework.stereotype.Entity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityReference
import java.util.UUID

class DungeonPortalEntity(
    override var id: Int,
    val dungeonWorld: ServerLevel,
    val leaveLocation: TeleportLocation,
    val portals: MutableList<EntityReference<PortalEntity>>,
    /**
     * Where each player entered the dungeon, so they can leave through the same portal again.
     * Kept in memory only: runtime dungeon worlds do not survive a server restart.
     */
    val entryLocations: MutableMap<UUID, TeleportLocation> = mutableMapOf(),
) : Entity<Int>
