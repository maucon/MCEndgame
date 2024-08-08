package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.technical.PersistentMapRepository
import java.util.*

@Service
class StatisticsRepository : PersistentMapRepository<UUID, StatisticsEntity>()
