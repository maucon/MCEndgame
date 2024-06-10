package de.fuballer.mcendgame.component.filter.db

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.PersistentMapRepository
import java.util.*

@Component
class FilterRepository : PersistentMapRepository<UUID, FilterEntity>()