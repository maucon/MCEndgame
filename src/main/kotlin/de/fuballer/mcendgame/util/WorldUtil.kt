package de.fuballer.mcendgame.util

import org.bukkit.World
import org.bukkit.entity.Entity
import java.util.*
import kotlin.reflect.KClass

object WorldUtil {
    fun getEntity(world: World, uuid: UUID) = world.entities.firstOrNull { it.uniqueId == uuid }

    fun getEntity(world: World, entityId: Int) = world.entities.firstOrNull { it.entityId == entityId }

    inline fun <reified T : Entity> getFilteredEntities(
        world: World,
        ids: List<UUID>,
        @Suppress("UNUSED_PARAMETER") instanceFilter: KClass<T>
    ): List<T> =
        ids.mapNotNull { getEntity(world, it) }
            .filterIsInstance<T>()
}