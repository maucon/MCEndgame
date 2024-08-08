package de.fuballer.mcendgame.component.dungeon.transition.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service

@Service
class DungeonTransitionRepository : InMemoryMapRepository<String, DungeonTransitionEntity>()
