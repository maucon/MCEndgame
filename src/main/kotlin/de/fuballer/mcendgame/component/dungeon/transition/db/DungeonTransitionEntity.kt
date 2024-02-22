package de.fuballer.mcendgame.component.dungeon.transition.db

import de.fuballer.mcendgame.component.portal.db.Portal
import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.Location

data class DungeonTransitionEntity(
    override var id: String,

    var portals: MutableList<Portal>,
    var leaveLocation: Location
) : Entity<String>
