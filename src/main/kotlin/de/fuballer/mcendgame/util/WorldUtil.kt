package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.dungeon.world.WorldSettings
import org.bukkit.World
import org.bukkit.entity.Entity
import java.util.*
import kotlin.reflect.KClass

object WorldUtil {
    fun isDungeonWorld(world: World) = world.name.contains(WorldSettings.WORLD_PREFIX)

    fun isNotDungeonWorld(world: World) = !isDungeonWorld(world)

    fun getEntity(world: World, uuid: UUID) = world.entities.firstOrNull { it.uniqueId == uuid }

    inline fun <reified T : Entity> getFilteredEntities(world: World, ids: Set<UUID>, instanceFilter: KClass<T>): List<T> =
        ids.mapNotNull { getEntity(world, it) }
            .filterIsInstance<T>()
}
