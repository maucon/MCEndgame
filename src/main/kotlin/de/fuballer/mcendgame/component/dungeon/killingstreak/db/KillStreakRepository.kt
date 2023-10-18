package de.fuballer.mcendgame.component.dungeon.killingstreak.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository

@Repository
class KillStreakRepository : AbstractMapRepository<String, KillStreakEntity>()
