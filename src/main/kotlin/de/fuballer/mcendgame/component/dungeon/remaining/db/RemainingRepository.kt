package de.fuballer.mcendgame.component.dungeon.remaining.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component

@Component
class RemainingRepository : InMemoryMapRepository<String, RemainingEntity>()
