package de.fuballer.mcendgame.component.dungeon.leave.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component

@Component
class DungeonLeaveRepository : InMemoryMapRepository<String, DungeonLeaveEntity>()
