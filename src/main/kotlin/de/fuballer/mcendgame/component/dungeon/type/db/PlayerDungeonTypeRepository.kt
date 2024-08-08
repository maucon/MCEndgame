package de.fuballer.mcendgame.component.dungeon.type.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import java.util.*

@Service
class PlayerDungeonTypeRepository : InMemoryMapRepository<UUID, PlayerDungeonTypeEntity>()
