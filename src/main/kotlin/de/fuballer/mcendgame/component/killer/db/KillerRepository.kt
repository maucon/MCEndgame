package de.fuballer.mcendgame.component.killer.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import java.util.*

@Repository
class KillerRepository : AbstractMapRepository<UUID, KillerEntity>()
