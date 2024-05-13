package de.fuballer.mcendgame.component.dungeon.remaining.db

import de.fuballer.mcendgame.framework.stereotype.Entity

data class RemainingEntity(
    override var id: String,

    var remaining: Int = 0,
    var bossesSlain: Int = 0,
    var dungeonCompleted: Boolean = false
) : Entity<String>
