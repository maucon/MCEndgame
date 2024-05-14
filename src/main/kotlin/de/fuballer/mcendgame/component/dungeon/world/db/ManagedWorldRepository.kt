package de.fuballer.mcendgame.component.dungeon.world.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component

@Component
class ManagedWorldRepository : InMemoryMapRepository<String, ManagedWorldEntity>()
