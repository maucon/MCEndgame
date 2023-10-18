package de.fuballer.mcendgame.component.dungeon.world.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository

@Repository
class WorldManageRepository : AbstractMapRepository<String, ManagedWorldEntity>()
