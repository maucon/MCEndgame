package de.fuballer.mcendgame.component.dungeon.artifact.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class HealOnBlockArtifactRepository : InMemoryMapRepository<UUID, HealOnBlockArtifactEntity>()
