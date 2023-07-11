package de.fuballer.mcendgame.component.remaining.db

import de.fuballer.mcendgame.framework.stereotype.Entity

data class RemainingEntity(
    override var id: String,

    var remaining: Int = 0,
    var bossAlive: Boolean = true
) : Entity<String>
