package de.fuballer.mcendgame.component.dungeon.player_list.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.World

@Component
class DungeonPlayerListRepository : InMemoryMapRepository<World, DungeonPlayerListEntity>()