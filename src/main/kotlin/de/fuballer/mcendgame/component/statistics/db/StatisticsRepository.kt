package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.domain.PersistableMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class StatisticsRepository : PersistableMapRepository<UUID, StatisticsEntity>()
