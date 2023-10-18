package de.fuballer.mcendgame.component.filter.db

import de.fuballer.mcendgame.domain.PersistableMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import java.util.*

@Repository
class FilterRepository : PersistableMapRepository<UUID, FilterEntity>()
