package de.fuballer.mcendgame.component.dungeon.world.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.World

data class ManagedWorldEntity(
    override var id: String,

    var world: World,
    var mapTier: Int,
    var deleteTimer: Int
) : Entity<String>
