package de.fuballer.mcendgame.component.dungeon.type.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class PlayerDungeonTypeRepository : InMemoryMapRepository<UUID, PlayerDungeonTypeEntity>()
