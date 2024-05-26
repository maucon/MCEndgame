package de.fuballer.mcendgame.component.custom_entity.ability.db

import de.fuballer.mcendgame.component.custom_entity.ability.runner.EntityRunner
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.framework.stereotype.Entity
import java.util.*

data class EntityAbilityEntity(
    override var id: UUID,

    var entityType: CustomEntityType,
    var runner: EntityRunner? = null
) : Entity<UUID>
