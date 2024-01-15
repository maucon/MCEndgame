package de.fuballer.mcendgame.component.custom_entity.ability.db

import de.fuballer.mcendgame.component.custom_entity.ability.EntityAbilityRunner
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.framework.stereotype.Entity
import java.util.*

data class EntityAbilityEntity(
    override var id: UUID,

    var entityType: CustomEntityType,
    var abilityRunner: EntityAbilityRunner? = null
) : Entity<UUID>
