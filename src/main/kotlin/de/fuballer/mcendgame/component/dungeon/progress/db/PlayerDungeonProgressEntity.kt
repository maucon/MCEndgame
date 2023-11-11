package de.fuballer.mcendgame.component.dungeon.progress.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import java.util.*

data class PlayerDungeonProgressEntity(
    override var id: UUID,

    var tier: Int = 1,
    var progress: Int = 0
) : Entity<UUID>
