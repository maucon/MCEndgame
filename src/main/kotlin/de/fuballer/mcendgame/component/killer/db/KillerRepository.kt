package de.fuballer.mcendgame.component.killer.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class KillerRepository : InMemoryMapRepository<UUID, KillerEntity>()
