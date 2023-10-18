package de.fuballer.mcendgame.component.filter.db

import de.fuballer.mcendgame.domain.PersistableMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class FilterRepository : PersistableMapRepository<UUID, FilterEntity>()
