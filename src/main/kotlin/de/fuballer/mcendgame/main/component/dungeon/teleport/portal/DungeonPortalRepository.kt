package de.fuballer.mcendgame.main.component.dungeon.teleport.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.stereotype.extension.InMemoryMapRepository
import net.minecraft.server.level.ServerLevel
import java.util.UUID

@Injectable
class DungeonPortalRepository : InMemoryMapRepository<Int, DungeonPortalEntity>() {
    fun findByDungeonWorld(world: ServerLevel): DungeonPortalEntity? =
        findAll().find { it.dungeonWorld == world }

    fun findEntryLocation(dungeonWorld: ServerLevel, playerId: UUID): TeleportLocation? =
        findByDungeonWorld(dungeonWorld)?.entryLocations?.get(playerId)
}
