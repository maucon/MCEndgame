package de.fuballer.mcendgame.component.dungeon.player_list.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.World

@Service
class DungeonPlayerListRepository : InMemoryMapRepository<World, DungeonPlayerListEntity>()