package de.fuballer.mcendgame.component.filter.db

import de.fuballer.mcendgame.component.technical.PersistentMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class FilterRepository : PersistentMapRepository<UUID, FilterEntity>()
