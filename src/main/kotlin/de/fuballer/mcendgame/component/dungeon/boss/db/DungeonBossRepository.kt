package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import java.util.*

@Repository
class DungeonBossRepository : AbstractMapRepository<UUID, DungeonBossEntity>()
