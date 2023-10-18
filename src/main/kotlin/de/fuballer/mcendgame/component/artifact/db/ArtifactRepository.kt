package de.fuballer.mcendgame.component.artifact.db

import de.fuballer.mcendgame.domain.PersistableMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import java.util.*

@Repository
class ArtifactRepository : PersistableMapRepository<UUID, ArtifactEntity>()
