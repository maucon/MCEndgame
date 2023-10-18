package de.fuballer.mcendgame.component.killer.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class KillerRepository : AbstractMapRepository<UUID, KillerEntity>()
