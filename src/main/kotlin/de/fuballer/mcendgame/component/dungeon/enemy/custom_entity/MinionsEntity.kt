package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.entity.LivingEntity
import java.util.UUID

class MinionsEntity(
    override var id: UUID,
    var minions: MutableSet<LivingEntity> = mutableSetOf(),
) : Entity<UUID>