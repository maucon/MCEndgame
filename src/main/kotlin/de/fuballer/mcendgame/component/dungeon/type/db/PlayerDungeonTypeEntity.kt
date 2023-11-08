package de.fuballer.mcendgame.component.dungeon.type.db

import de.fuballer.mcendgame.component.dungeon.type.data.DungeonType
import de.fuballer.mcendgame.framework.stereotype.Entity
import java.util.*

data class PlayerDungeonTypeEntity(
    override var id: UUID,

    val dungeonType: DungeonType
) : Entity<UUID>
