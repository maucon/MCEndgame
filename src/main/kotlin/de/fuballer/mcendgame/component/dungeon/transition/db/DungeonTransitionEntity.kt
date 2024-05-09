package de.fuballer.mcendgame.component.dungeon.transition.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.Location

data class DungeonTransitionEntity(
    override var id: String,

    var respawnLocation: Location
) : Entity<String>
