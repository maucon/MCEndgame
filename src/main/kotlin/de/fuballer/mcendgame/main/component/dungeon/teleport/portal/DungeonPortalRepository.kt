package de.fuballer.mcendgame.main.component.dungeon.teleport.portal

import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.stereotype.extension.InMemoryMapRepository
import net.minecraft.server.level.ServerLevel

@Injectable
class DungeonPortalRepository : InMemoryMapRepository<Int, DungeonPortalEntity>() {
    fun findByDungeonWorld(world: ServerLevel): DungeonPortalEntity? =
        findAll().find { it.dungeonWorld == world }
}