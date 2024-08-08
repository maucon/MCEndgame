package de.fuballer.mcendgame.component.dungeon.progress.db

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.technical.PersistentMapRepository
import java.util.*

@Service
class PlayerDungeonProgressRepository : PersistentMapRepository<UUID, PlayerDungeonProgressEntity>()
