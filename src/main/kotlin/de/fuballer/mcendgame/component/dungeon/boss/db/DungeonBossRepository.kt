package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class DungeonBossRepository : InMemoryMapRepository<UUID, DungeonBossEntity>()
