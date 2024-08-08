package de.fuballer.mcendgame.component.dungeon.killstreak.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service

@Service
class KillStreakRepository : InMemoryMapRepository<String, KillStreakEntity>()