package de.fuballer.mcendgame.main.component.dungeon.teleport.portal

import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.maucon.mauconframework.stereotype.Entity
import net.minecraft.server.world.ServerWorld

class DungeonPortalEntity(
    override var id: Int,
    val dungeonWorld: ServerWorld,
    val leaveLocation: TeleportLocation,
    val portals: MutableList<PortalEntity>
) : Entity<Int>