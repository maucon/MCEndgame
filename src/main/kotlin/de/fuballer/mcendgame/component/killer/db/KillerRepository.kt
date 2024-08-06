package de.fuballer.mcendgame.component.killer.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import java.util.*

@Service
class KillerRepository : InMemoryMapRepository<UUID, KillerEntity>()
