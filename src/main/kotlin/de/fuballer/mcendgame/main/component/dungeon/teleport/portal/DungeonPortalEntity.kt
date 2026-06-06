package de.fuballer.mcendgame.main.component.dungeon.teleport.portal

import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.maucon.mauconframework.stereotype.Entity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityReference

class DungeonPortalEntity(
    override var id: Int,
    val dungeonWorld: ServerLevel,
    val leaveLocation: TeleportLocation,
    val portals: MutableList<EntityReference<PortalEntity>>
) : Entity<Int>