package de.fuballer.mcendgame.component.dungeon.killingstreak.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component

@Component
class KillStreakRepository : InMemoryMapRepository<String, KillStreakEntity>()
