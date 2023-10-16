package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.dungeon.world.WorldSettings
import org.bukkit.World

object WorldUtil {
    fun isDungeonWorld(world: World) = world.name.contains(WorldSettings.WORLD_PREFIX)

    fun isNotDungeonWorld(world: World) = !isDungeonWorld(world)
}
