package de.fuballer.mcendgame.component.custom_entity.ability.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import java.util.*

@Component
class EntityAbilityRepository : InMemoryMapRepository<UUID, EntityAbilityEntity>()
