package de.fuballer.mcendgame.component.custom_entity.summoner.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.entity.LivingEntity
import java.util.*

class MinionsEntity(
    override var id: UUID,

    var minions: MutableSet<LivingEntity> = mutableSetOf(),
) : Entity<UUID>
