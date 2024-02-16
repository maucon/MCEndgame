package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.dungeon.world.WorldSettings
import org.bukkit.World

object WorldExtension {
    fun World.isDungeonWorld() = name.contains(WorldSettings.WORLD_PREFIX)
}