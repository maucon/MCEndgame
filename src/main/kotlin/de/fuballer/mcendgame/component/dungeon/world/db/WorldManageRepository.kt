package de.fuballer.mcendgame.component.dungeon.world.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Component

@Component
class WorldManageRepository : AbstractMapRepository<String, ManagedWorldEntity>()
