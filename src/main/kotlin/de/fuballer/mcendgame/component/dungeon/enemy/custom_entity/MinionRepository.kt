package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import java.util.*

@Repository
class MinionRepository : AbstractMapRepository<UUID, MinionsEntity>()