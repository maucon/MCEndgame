package de.fuballer.mcendgame.component.dungeon.progress.db

import de.fuballer.mcendgame.domain.PersistableMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class PlayerDungeonProgressRepository : PersistableMapRepository<UUID, PlayerDungeonProgressEntity>()
