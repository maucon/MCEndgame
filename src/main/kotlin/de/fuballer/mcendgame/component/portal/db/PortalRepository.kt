package de.fuballer.mcendgame.component.portal.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import java.util.*

@Service
class PortalRepository : InMemoryMapRepository<UUID, Portal>()