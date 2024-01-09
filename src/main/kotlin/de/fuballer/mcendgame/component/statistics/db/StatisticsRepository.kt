package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.domain.PersistentMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class StatisticsRepository : PersistentMapRepository<UUID, StatisticsEntity>()
