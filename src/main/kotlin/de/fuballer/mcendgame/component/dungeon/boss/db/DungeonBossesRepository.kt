package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.World
import java.util.*

@Component
class DungeonBossesRepository : InMemoryMapRepository<UUID, DungeonBossesEntity>() {
    fun findByWorld(world: World) = findAll()
        .find { it.world.name == world.name }
}
