package de.fuballer.mcendgame.component.dungeon.artifact.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class HealOnBlockArtifactRepository : AbstractMapRepository<UUID, HealOnBlockArtifactEntity>()
