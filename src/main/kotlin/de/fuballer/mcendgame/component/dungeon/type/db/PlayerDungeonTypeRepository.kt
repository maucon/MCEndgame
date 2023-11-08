package de.fuballer.mcendgame.component.dungeon.type.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class PlayerDungeonTypeRepository : AbstractMapRepository<UUID, PlayerDungeonTypeEntity>()
