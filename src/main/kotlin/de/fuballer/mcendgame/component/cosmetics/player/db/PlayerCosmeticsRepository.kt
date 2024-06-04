package de.fuballer.mcendgame.component.cosmetics.player.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class PlayerCosmeticsRepository : InMemoryMapRepository<UUID, PlayerCosmeticsEntity>()
