package de.fuballer.mcendgame.component.dungeon.progress.db

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.PersistentMapRepository
import java.util.*

@Component
class PlayerDungeonProgressRepository : PersistentMapRepository<UUID, PlayerDungeonProgressEntity>()
