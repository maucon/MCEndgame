package de.fuballer.mcendgame.component.dungeon.transition.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component

@Component
class DungeonTransitionRepository : InMemoryMapRepository<String, DungeonTransitionEntity>()
