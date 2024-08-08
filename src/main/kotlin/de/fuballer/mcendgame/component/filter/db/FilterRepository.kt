package de.fuballer.mcendgame.component.filter.db

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.technical.PersistentMapRepository
import java.util.*

@Service
class FilterRepository : PersistentMapRepository<UUID, FilterEntity>()