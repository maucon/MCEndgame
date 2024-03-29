package de.fuballer.mcendgame.component.dungeon.seed.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class DungeonSeedRepository : InMemoryMapRepository<UUID, DungeonSeedEntity>()
