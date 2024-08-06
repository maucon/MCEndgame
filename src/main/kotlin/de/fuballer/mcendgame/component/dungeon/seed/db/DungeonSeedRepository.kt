package de.fuballer.mcendgame.component.dungeon.seed.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import java.util.*

@Service
class DungeonSeedRepository : InMemoryMapRepository<UUID, DungeonSeedEntity>()
