package de.fuballer.mcendgame.component.dungeon.progress.db

import de.fuballer.mcendgame.domain.PersistableMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import java.util.*

@Repository
class PlayerDungeonProgressRepository : PersistableMapRepository<UUID, PlayerDungeonProgressEntity>()
