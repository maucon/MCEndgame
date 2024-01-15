package de.fuballer.mcendgame.component.artifact.db

import de.fuballer.mcendgame.domain.PersistentMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class ArtifactRepository : PersistentMapRepository<UUID, ArtifactEntity>()
