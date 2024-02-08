package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.PersistentMapRepository
import java.util.*

@Component
class StatisticsRepository : PersistentMapRepository<UUID, StatisticsEntity>()
