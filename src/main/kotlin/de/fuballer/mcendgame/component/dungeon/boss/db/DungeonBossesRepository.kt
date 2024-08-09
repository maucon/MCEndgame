package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.World
import java.util.*

@Service
class DungeonBossesRepository : InMemoryMapRepository<UUID, DungeonBossesEntity>() {
    fun findByWorld(world: World) = findAll()
        .find { it.world.name == world.name }
}
