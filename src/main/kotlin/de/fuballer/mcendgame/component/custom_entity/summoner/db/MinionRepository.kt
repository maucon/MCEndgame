package de.fuballer.mcendgame.component.custom_entity.summoner.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class MinionRepository : InMemoryMapRepository<UUID, MinionsEntity>()
