package de.fuballer.mcendgame.component.custom_entity.ability.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import java.util.*

@Service
class EntityAbilityRepository : InMemoryMapRepository<UUID, EntityAbilityEntity>()
