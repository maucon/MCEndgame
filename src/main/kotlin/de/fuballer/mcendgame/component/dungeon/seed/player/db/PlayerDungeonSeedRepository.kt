package de.fuballer.mcendgame.component.dungeon.seed.player.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class PlayerDungeonSeedRepository : InMemoryMapRepository<UUID, PlayerDungeonSeedEntity>()
