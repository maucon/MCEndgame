package de.fuballer.mcendgame.component.dungeon.world.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.World
import org.bukkit.entity.Player

data class ManagedWorldEntity(
    override var id: String,

    var player: Player,
    var world: World,
    var mapTier: Int,
    var deleteTimer: Int
) : Entity<String>
