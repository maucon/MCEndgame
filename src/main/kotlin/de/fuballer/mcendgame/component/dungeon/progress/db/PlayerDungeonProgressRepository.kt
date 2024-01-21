package de.fuballer.mcendgame.component.dungeon.progress.db

import de.fuballer.mcendgame.domain.technical.PersistentMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class PlayerDungeonProgressRepository : PersistentMapRepository<UUID, PlayerDungeonProgressEntity>()
