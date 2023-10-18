package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.domain.PersistableMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import java.util.*

@Repository
class StatisticsRepository : PersistableMapRepository<UUID, StatisticsEntity>()
