package de.fuballer.mcendgame.component.dungeon.seed.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import java.util.*

data class PlayerDungeonSeedEntity(
    override var id: UUID,

    val seed: String
) : Entity<UUID>
