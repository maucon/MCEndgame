package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class DungeonBossRepository : AbstractMapRepository<UUID, DungeonBossEntity>()
