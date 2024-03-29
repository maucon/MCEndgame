package de.fuballer.mcendgame.component.portal.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class PortalRepository : InMemoryMapRepository<UUID, Portal>()